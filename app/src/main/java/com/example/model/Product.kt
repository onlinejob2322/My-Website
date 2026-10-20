package com.example.model

data class Product(
    val id: String,
    val name: String,
    val nameBn: String,
    val description: String,
    val descriptionBn: String,
    val price: Double,
    val originalPrice: Double,
    val discountPercent: Int,
    val category: String,
    val categoryBn: String,
    val image: String,
    val stock: Int,
    val rating: Double,
    val reviewsCount: Int,
    val badge: String = "",
    val isFeatured: Boolean = false,
    val specs: Map<String, String> = emptyMap()
)

data class BannerItem(
    val id: String,
    val title: String,
    val titleBn: String,
    val subtitle: String,
    val subtitleBn: String,
    val buttonText: String,
    val buttonTextBn: String,
    val imageUrl: String,
    val badge: String,
    val targetCategory: String = "all"
)

data class ProductCategory(
    val id: String,
    val name: String,
    val nameBn: String,
    val iconName: String
)
