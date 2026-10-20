package com.example.data

import com.example.model.CartItem
import com.example.model.DeliveryArea
import com.example.model.Order
import com.example.model.OrderItemRecord
import com.example.model.OrderStatus
import com.example.model.PaymentMethod
import com.example.model.Product
import com.example.model.StoreSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ShopRepository {

    private val _products = MutableStateFlow<List<Product>>(SampleData.initialProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(SampleData.sampleInitialOrders)
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _settings = MutableStateFlow(StoreSettings())
    val settings: StateFlow<StoreSettings> = _settings.asStateFlow()

    private val _isBengali = MutableStateFlow(true) // Default to Bengali as seen on aminurbdshop.netlify.app
    val isBengali: StateFlow<Boolean> = _isBengali.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<String?>(null)
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.asStateFlow()

    private val _wishlist = MutableStateFlow<Set<String>>(emptySet())
    val wishlist: StateFlow<Set<String>> = _wishlist.asStateFlow()

    fun toggleLanguage() {
        _isBengali.value = !_isBengali.value
    }

    fun toggleWishlist(productId: String) {
        val current = _wishlist.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _wishlist.value = current
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val existing = current[index]
            val newQty = (existing.quantity + quantity).coerceAtMost(product.stock)
            current[index] = existing.copy(quantity = newQty)
        } else {
            current.add(CartItem(product = product, quantity = quantity.coerceAtMost(product.stock)))
        }
        _cartItems.value = current
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index != -1) {
            val item = current[index]
            val newQty = item.quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                val capped = newQty.coerceAtMost(item.product.stock)
                current[index] = item.copy(quantity = capped)
            }
            _cartItems.value = current
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _appliedCoupon.value = null
    }

    fun applyCoupon(code: String): Boolean {
        val cleanCode = code.trim().uppercase()
        if (_settings.value.promoCodes.containsKey(cleanCode)) {
            _appliedCoupon.value = cleanCode
            return true
        }
        return false
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    fun placeOrder(
        customerName: String,
        phone: String,
        address: String,
        district: String,
        deliveryArea: DeliveryArea,
        paymentMethod: PaymentMethod,
        transactionId: String = "",
        note: String = ""
    ): Order {
        val items = _cartItems.value.map {
            OrderItemRecord(
                productId = it.product.id,
                productName = it.product.name,
                productNameBn = it.product.nameBn,
                price = it.product.price,
                quantity = it.quantity,
                image = it.product.image
            )
        }

        val subtotal = _cartItems.value.sumOf { it.totalPrice }
        val fee = if (subtotal >= _settings.value.freeDeliveryThreshold) {
            0.0
        } else {
            deliveryArea.fee
        }

        var discount = 0.0
        _appliedCoupon.value?.let { code ->
            val rule = _settings.value.promoCodes[code]
            if (rule != null) {
                discount = if (rule < 1.0) {
                    subtotal * rule
                } else {
                    rule
                }
            }
        }

        val total = (subtotal + fee - discount).coerceAtLeast(0.0)
        val randomSuffix = Random.nextInt(10000, 99999)
        val orderId = "ABD-$randomSuffix"

        val newOrder = Order(
            id = orderId,
            customerName = customerName,
            phone = phone,
            address = address,
            district = district,
            deliveryArea = deliveryArea,
            paymentMethod = paymentMethod,
            transactionId = transactionId,
            note = note,
            items = items,
            subtotal = subtotal,
            deliveryFee = fee,
            discount = discount,
            total = total,
            status = OrderStatus.PENDING,
            courierName = "Steadfast Courier",
            courierTrackingCode = "STDF-${Random.nextInt(100000, 999999)}"
        )

        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        return newOrder
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(status = newStatus)
            } else {
                order
            }
        }
    }

    fun updateProductStock(productId: String, newStock: Int) {
        _products.value = _products.value.map { prod ->
            if (prod.id == productId) {
                prod.copy(stock = newStock)
            } else {
                prod
            }
        }
    }

    fun updateProductPrice(productId: String, newPrice: Double) {
        _products.value = _products.value.map { prod ->
            if (prod.id == productId) {
                val discount = if (prod.originalPrice > newPrice) {
                    (((prod.originalPrice - newPrice) / prod.originalPrice) * 100).toInt()
                } else 0
                prod.copy(price = newPrice, discountPercent = discount)
            } else {
                prod
            }
        }
    }

    fun addProduct(product: Product) {
        _products.value = listOf(product) + _products.value
    }

    fun findOrderByTracking(query: String): Order? {
        val q = query.trim().uppercase()
        if (q.isEmpty()) return null
        return _orders.value.firstOrNull {
            it.id.uppercase() == q || it.phone.replace("-", "").contains(q.replace("-", ""))
        }
    }
}
