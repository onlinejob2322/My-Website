package com.example.model

data class StoreSettings(
    val storeName: String = "Aminur BD Shop",
    val storeNameBn: String = "আমিনুর বিডি শপ",
    val tagline: String = "বাংলাদেশের বিশ্বস্ত প্রিমিয়াম অনলাইন শপ",
    val bkashNumber: String = "01712-345678",
    val nagadNumber: String = "01712-345678",
    val hotlinePhone: String = "+880 1712-345678",
    val whatsappNumber: String = "+880 1712-345678",
    val adminEmail: String = "aminurahamed52@gmail.com",
    val adminPasscode: String = "1234",
    val insideDhakaFee: Double = 60.0,
    val outsideDhakaFee: Double = 120.0,
    val freeDeliveryThreshold: Double = 2500.0,
    val promoCodes: Map<String, Double> = mapOf(
        "AMINUR10" to 0.10, // 10% off
        "WELCOME" to 100.0  // ৳100 flat off
    )
)
