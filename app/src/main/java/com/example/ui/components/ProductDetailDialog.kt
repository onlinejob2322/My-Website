package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.Product
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.Gold500
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Navy950
import com.example.ui.theme.Slate100

@Composable
fun ProductDetailDialog(
    product: Product,
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onAddToCart: (Int) -> Unit,
    onBuyNow: (Int) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header with Close Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Slate100)
                ) {
                    AsyncImage(
                        model = product.image,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Close Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(36.dp)
                            .background(Navy900.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Badge
                    if (product.badge.isNotEmpty()) {
                        Surface(
                            color = Navy900,
                            shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 8.dp),
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Text(
                                text = product.badge,
                                color = Gold500,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Scrollable Details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Rating & Stock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = AmberAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${product.rating}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Navy900
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(${product.reviewsCount} ${if (isBengali) "রিভিউ" else "reviews"})",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Surface(
                            color = if (product.stock > 0) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (product.stock > 0) {
                                    if (isBengali) "স্টকে আছে: ${product.stock} টি" else "In Stock: ${product.stock}"
                                } else {
                                    if (isBengali) "স্টক আউট" else "Out of Stock"
                                },
                                color = if (product.stock > 0) Color(0xFF166534) else Color(0xFF991B1B),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Title
                    Text(
                        text = if (isBengali) product.nameBn else product.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pricing
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "৳${product.price.toInt()}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Navy900
                        )
                        if (product.originalPrice > product.price) {
                            Text(
                                text = "৳${product.originalPrice.toInt()}",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                            Surface(
                                color = Color(0xFFEF4444),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${product.discountPercent}% OFF",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Header
                    Text(
                        text = if (isBengali) "বিবরণ ও বৈশিষ্ট্য" else "Description & Features",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBengali) product.descriptionBn else product.description,
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        lineHeight = 19.sp
                    )

                    // Specs list
                    if (product.specs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (isBengali) "স্পেসিফিকেশন" else "Specifications",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                product.specs.forEach { (key, value) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = key,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = value,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Navy900
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quantity Stepper
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isBengali) "পরিমাণ নির্বাচন করুন:" else "Select Quantity:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Navy900
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Slate100, RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Navy900)
                            }
                            Text(
                                text = "$quantity",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            IconButton(
                                onClick = { if (quantity < product.stock) quantity++ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = Navy900)
                            }
                        }
                    }
                }

                // Bottom Buttons Bar
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onAddToCart(quantity)
                                onDismiss()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy900),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("details_add_to_cart_btn")
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "কার্ট-এ যোগ করুন" else "Add to Cart",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = {
                                onBuyNow(quantity)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Navy950,
                                contentColor = Gold500
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp)
                                .testTag("details_buy_now_btn")
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "অর্ডার করুন এখনই" else "Order Now",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
