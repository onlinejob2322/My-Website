package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.data.ShopRepository
import com.example.model.BannerItem
import com.example.model.CartItem
import com.example.model.DeliveryArea
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.PaymentMethod
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.StoreSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ShopScreen {
    HOME,
    CATALOG,
    TRACK,
    ADMIN
}

class ShopViewModel(
    val repository: ShopRepository = ShopRepository()
) : ViewModel() {

    val isBengali: StateFlow<Boolean> = repository.isBengali
    val settings: StateFlow<StoreSettings> = repository.settings
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val orders: StateFlow<List<Order>> = repository.orders
    val appliedCoupon: StateFlow<String?> = repository.appliedCoupon
    val wishlist: StateFlow<Set<String>> = repository.wishlist

    val banners: List<BannerItem> = SampleData.banners
    val categories: List<ProductCategory> = SampleData.categories

    private val _currentScreen = MutableStateFlow(ShopScreen.HOME)
    val currentScreen: StateFlow<ShopScreen> = _currentScreen.asStateFlow()

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Dialog & Sheet States
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _isCartOpen = MutableStateFlow(false)
    val isCartOpen: StateFlow<Boolean> = _isCartOpen.asStateFlow()

    private val _isCheckoutOpen = MutableStateFlow(false)
    val isCheckoutOpen: StateFlow<Boolean> = _isCheckoutOpen.asStateFlow()

    private val _successOrder = MutableStateFlow<Order?>(null)
    val successOrder: StateFlow<Order?> = _successOrder.asStateFlow()

    // Tracking screen state
    private val _trackingInput = MutableStateFlow("")
    val trackingInput: StateFlow<String> = _trackingInput.asStateFlow()

    private val _trackedOrder = MutableStateFlow<Order?>(null)
    val trackedOrder: StateFlow<Order?> = _trackedOrder.asStateFlow()

    private val _trackingSearched = MutableStateFlow(false)
    val trackingSearched: StateFlow<Boolean> = _trackingSearched.asStateFlow()

    // Admin state
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _adminPasscodeInput = MutableStateFlow("")
    val adminPasscodeInput: StateFlow<String> = _adminPasscodeInput.asStateFlow()

    private val _adminError = MutableStateFlow<String?>(null)
    val adminError: StateFlow<String?> = _adminError.asStateFlow()

    // Toast / Banner message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Filtered products
    val filteredProducts: StateFlow<List<Product>> = combine(
        repository.products,
        _selectedCategory,
        _searchQuery
    ) { prods, cat, query ->
        prods.filter { prod ->
            val matchesCat = (cat == "all" || prod.category.equals(cat, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    prod.name.contains(query, ignoreCase = true) ||
                    prod.nameBn.contains(query, ignoreCase = true) ||
                    prod.categoryBn.contains(query, ignoreCase = true)
            matchesCat && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.products.value)

    fun navigateTo(screen: ShopScreen) {
        _currentScreen.value = screen
    }

    fun selectCategory(catId: String) {
        _selectedCategory.value = catId
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleLanguage() {
        repository.toggleLanguage()
    }

    fun toggleWishlist(productId: String) {
        repository.toggleWishlist(productId)
    }

    fun openProductDetails(product: Product) {
        _selectedProduct.value = product
    }

    fun closeProductDetails() {
        _selectedProduct.value = null
    }

    fun openCart() {
        _isCartOpen.value = true
    }

    fun closeCart() {
        _isCartOpen.value = false
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        repository.addToCart(product, quantity)
        val msg = if (isBengali.value) "${product.nameBn} কার্ট-এ যুক্ত হয়েছে!" else "${product.name} added to cart!"
        _userMessage.value = msg
    }

    fun buyNow(product: Product) {
        repository.addToCart(product, 1)
        closeProductDetails()
        _isCartOpen.value = false
        _isCheckoutOpen.value = true
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        repository.updateCartQuantity(productId, delta)
    }

    fun removeFromCart(productId: String) {
        repository.removeFromCart(productId)
    }

    fun applyCoupon(code: String) {
        val success = repository.applyCoupon(code)
        if (success) {
            _userMessage.value = if (isBengali.value) "কুপন কোড সফলভাবে অ্যাপ্লাই হয়েছে!" else "Coupon applied successfully!"
        } else {
            _userMessage.value = if (isBengali.value) "ভুল কুপন কোড! ট্রাই করুন: AMINUR10" else "Invalid coupon code! Try: AMINUR10"
        }
    }

    fun removeCoupon() {
        repository.removeCoupon()
    }

    fun openCheckout() {
        if (cartItems.value.isEmpty()) {
            _userMessage.value = if (isBengali.value) "আপনার কার্ট খালি!" else "Your cart is empty!"
            return
        }
        _isCartOpen.value = false
        _isCheckoutOpen.value = true
    }

    fun closeCheckout() {
        _isCheckoutOpen.value = false
    }

    fun submitOrder(
        customerName: String,
        phone: String,
        address: String,
        district: String,
        deliveryArea: DeliveryArea,
        paymentMethod: PaymentMethod,
        transactionId: String,
        note: String
    ) {
        val order = repository.placeOrder(
            customerName = customerName,
            phone = phone,
            address = address,
            district = district,
            deliveryArea = deliveryArea,
            paymentMethod = paymentMethod,
            transactionId = transactionId,
            note = note
        )
        _isCheckoutOpen.value = false
        _successOrder.value = order
    }

    fun dismissSuccessOrder() {
        _successOrder.value = null
    }

    // Tracking
    fun updateTrackingInput(text: String) {
        _trackingInput.value = text
    }

    fun searchOrderTracking() {
        _trackingSearched.value = true
        _trackedOrder.value = repository.findOrderByTracking(_trackingInput.value)
    }

    fun trackSpecificOrder(orderId: String) {
        _trackingInput.value = orderId
        _trackingSearched.value = true
        _trackedOrder.value = repository.findOrderByTracking(orderId)
        _currentScreen.value = ShopScreen.TRACK
        _successOrder.value = null
    }

    // Admin Auth & Ops
    fun updateAdminPasscode(pin: String) {
        _adminPasscodeInput.value = pin
        _adminError.value = null
    }

    fun loginAdmin() {
        if (_adminPasscodeInput.value.trim() == settings.value.adminPasscode) {
            _isAdminAuthenticated.value = true
            _adminPasscodeInput.value = ""
            _adminError.value = null
        } else {
            _adminError.value = if (isBengali.value) "ভুল পিন কোড! ডিফল্ট পিন: 1234" else "Invalid Passcode! Default PIN is 1234"
        }
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        _adminPasscodeInput.value = ""
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        repository.updateOrderStatus(orderId, status)
        _userMessage.value = if (isBengali.value) "অর্ডার স্ট্যাটাস আপডেট হয়েছে: ${status.titleBn}" else "Order status updated to ${status.titleEn}"
    }

    fun updateProductStock(productId: String, stock: Int) {
        repository.updateProductStock(productId, stock)
    }

    fun updateProductPrice(productId: String, price: Double) {
        repository.updateProductPrice(productId, price)
    }

    fun addNewProduct(product: Product) {
        repository.addProduct(product)
        _userMessage.value = if (isBengali.value) "নতুন পণ্য সফলভাবে যুক্ত হয়েছে!" else "New product added successfully!"
    }

    fun dismissMessage() {
        _userMessage.value = null
    }
}
