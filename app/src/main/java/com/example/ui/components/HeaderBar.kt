package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ShopScreen
import com.example.ui.ShopViewModel
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Navy950

@Composable
fun HeaderBar(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val isBn = viewModel.isBengali.value
    val cartCount = viewModel.cartItems.value.sumOf { it.quantity }
    val searchQuery = viewModel.searchQuery.value

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Navy950, Navy900)
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Branding Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.navigateTo(ShopScreen.HOME) }
                    .padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Gold500),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Logo",
                        tint = Navy950,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AMINUR BD",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "SHOP",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Gold400,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = if (isBn) "প্রিমিয়াম অনলাইন শপিং" else "Premium Online Shopping",
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
            }

            // Right Action Buttons: Language toggle, Cart with badge, Admin Lock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language Switcher Pill
                Surface(
                    color = Navy800,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .testTag("lang_toggle_button")
                        .clickable { viewModel.toggleLanguage() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBn) "বাং" else "EN",
                            color = Gold400,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Change Language",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Cart Icon with Badge
                IconButton(
                    onClick = { viewModel.openCart() },
                    modifier = Modifier
                        .testTag("cart_button")
                        .size(40.dp)
                ) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = Gold500,
                                    contentColor = Navy950
                                ) {
                                    Text(
                                        text = cartCount.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart",
                            tint = Color.White
                        )
                    }
                }

                // Admin Button
                IconButton(
                    onClick = { viewModel.navigateTo(ShopScreen.ADMIN) },
                    modifier = Modifier
                        .testTag("admin_panel_button")
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = if (viewModel.isAdminAuthenticated.value) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                        contentDescription = "Admin Panel",
                        tint = if (viewModel.isAdminAuthenticated.value) Gold400 else Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Input Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = {
                Text(
                    text = if (isBn) "সার্চ করুন স্মার্টওয়াচ, পারফিউম, ফ্যাশন..." else "Search smartwatches, perfumes, fashion...",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Gold400
                )
            },
            trailingIcon = {
                AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Search",
                            tint = Color.LightGray
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Navy800,
                unfocusedContainerColor = Navy800,
                focusedBorderColor = Gold400,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("search_text_input")
        )
    }
}
