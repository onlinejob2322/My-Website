package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Product
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.Gold500
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate100

@Composable
fun ProductCard(
    product: Product,
    isBengali: Boolean,
    isWishlisted: Boolean,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    onToggleWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
            .testTag("product_card_${product.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image Box with Badge & Wishlist
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Slate100)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Left Badge
                if (product.badge.isNotEmpty()) {
                    Surface(
                        color = Navy900,
                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = product.badge,
                            color = Gold500,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Wishlist Icon Button
                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.85f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Discount Pill
                if (product.discountPercent > 0) {
                    Surface(
                        color = Color(0xFFEF4444),
                        shape = RoundedCornerShape(topEnd = 6.dp, topStart = 6.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 6.dp)
                    ) {
                        Text(
                            text = "-${product.discountPercent}%",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Info Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Category & Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBengali) product.categoryBn else product.category.replaceFirstChar { it.uppercase() },
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AmberAccent,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${product.rating}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy800
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Product Title
                Text(
                    text = if (isBengali) product.nameBn else product.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Navy900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp,
                    modifier = Modifier.height(36.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Price Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "৳${product.price.toInt()}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Navy900
                    )
                    if (product.originalPrice > product.price) {
                        Text(
                            text = "৳${product.originalPrice.toInt()}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick Buy Now Button
                    Button(
                        onClick = onBuyNow,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Navy900,
                            contentColor = Gold500
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .testTag("buy_now_${product.id}")
                    ) {
                        Text(
                            text = if (isBengali) "অর্ডার করুন" else "Buy Now",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Quick Add to Cart Button
                    FilledIconButton(
                        onClick = onAddToCart,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Gold500,
                            contentColor = Navy900
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("add_cart_${product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Add to Cart",
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}
