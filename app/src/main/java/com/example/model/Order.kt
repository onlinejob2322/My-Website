package com.example.model

enum class PaymentMethod(val labelEn: String, val labelBn: String) {
    COD("Cash on Delivery", "ক্যাশ অন ডেলিভারি"),
    BKASH("bKash Personal", "বিকাশ"),
    NAGAD("Nagad Personal", "নগদ")
}

enum class DeliveryArea(val labelEn: String, val labelBn: String, val fee: Double) {
    INSIDE_DHAKA("Inside Dhaka", "ঢাকা সিটির ভেতরে", 60.0),
    OUTSIDE_DHAKA("Outside Dhaka", "ঢাকা সিটির বাইরে", 120.0)
}

enum class OrderStatus(val titleEn: String, val titleBn: String, val stepIndex: Int) {
    PENDING("Order Placed", "অর্ডার গৃহীত", 1),
    CONFIRMED("Confirmed", "কনফার্ম করা হয়েছে", 2),
    PROCESSING("Processing", "প্যাকিং চলছে", 3),
    SHIPPED("Handed to Courier", "কুরিয়ারে হস্তান্তর", 4),
    OUT_FOR_DELIVERY("Out for Delivery", "ডেলিভারির জন্য বের হয়েছে", 5),
    DELIVERED("Delivered", "ডেলিভারি সম্পন্ন", 6),
    CANCELLED("Cancelled", "বাতিল", 0)
}

data class OrderItemRecord(
    val productId: String,
    val productName: String,
    val productNameBn: String,
    val price: Double,
    val quantity: Int,
    val image: String
)

data class Order(
    val id: String,
    val customerName: String,
    val phone: String,
    val address: String,
    val district: String,
    val deliveryArea: DeliveryArea,
    val paymentMethod: PaymentMethod,
    val transactionId: String = "",
    val note: String = "",
    val items: List<OrderItemRecord>,
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val total: Double,
    val status: OrderStatus = OrderStatus.PENDING,
    val courierName: String = "Steadfast Courier",
    val courierTrackingCode: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
