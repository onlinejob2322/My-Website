package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Product
import com.example.ui.ShopScreen
import com.example.ui.ShopViewModel
import com.example.ui.components.*
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Navy950
import com.example.ui.theme.Slate100

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ShopApp()
            }
        }
    }
}

@Composable
fun ShopApp(viewModel: ShopViewModel = viewModel()) {
    val context = LocalContext.current
    val currentScreen = viewModel.currentScreen.collectAsState().value
    val isBn = viewModel.isBengali.collectAsState().value
    val cartCount = viewModel.cartItems.collectAsState().value.sumOf { it.quantity }
    val userMsg = viewModel.userMessage.collectAsState().value

    // Back handling
    BackHandler(enabled = currentScreen != ShopScreen.HOME) {
        viewModel.navigateTo(ShopScreen.HOME)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(userMsg) {
        userMsg?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.dismissMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            HeaderBar(viewModel = viewModel)
        },
        bottomBar = {
            NavigationBar(
                containerColor = Navy950,
                contentColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                // Home Tab
                NavigationBarItem(
                    selected = currentScreen == ShopScreen.HOME,
                    onClick = { viewModel.navigateTo(ShopScreen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(if (isBn) "হোম" else "Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Navy950,
                        selectedTextColor = Gold400,
                        indicatorColor = Gold500,
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray
                    )
                )

                // Catalog Tab
                NavigationBarItem(
                    selected = currentScreen == ShopScreen.CATALOG,
                    onClick = { viewModel.navigateTo(ShopScreen.CATALOG) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Catalog") },
                    label = { Text(if (isBn) "ক্যাটালগ" else "Catalog", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Navy950,
                        selectedTextColor = Gold400,
                        indicatorColor = Gold500,
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray
                    )
                )

                // Track Order Tab
                NavigationBarItem(
                    selected = currentScreen == ShopScreen.TRACK,
                    onClick = { viewModel.navigateTo(ShopScreen.TRACK) },
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Track") },
                    label = { Text(if (isBn) "ট্র্যাকিং" else "Track", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Navy950,
                        selectedTextColor = Gold400,
                        indicatorColor = Gold500,
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray
                    )
                )

                // Cart Tab
                NavigationBarItem(
                    selected = false,
                    onClick = { viewModel.openCart() },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge(
                                        containerColor = Gold500,
                                        contentColor = Navy950
                                    ) {
                                        Text("$cartCount", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text(if (isBn) "কার্ট" else "Cart", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray
                    )
                )

                // Admin Tab
                NavigationBarItem(
                    selected = currentScreen == ShopScreen.ADMIN,
                    onClick = { viewModel.navigateTo(ShopScreen.ADMIN) },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                    label = { Text(if (isBn) "অ্যাডমিন" else "Admin", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Navy950,
                        selectedTextColor = Gold400,
                        indicatorColor = Gold500,
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ShopScreen.HOME -> HomeScreen(viewModel = viewModel)
                ShopScreen.CATALOG -> CatalogScreen(viewModel = viewModel)
                ShopScreen.TRACK -> TrackOrderScreen(viewModel = viewModel)
                ShopScreen.ADMIN -> AdminPanelScreen(viewModel = viewModel)
            }
        }
    }

    // Modal Overlays
    val selectedProduct = viewModel.selectedProduct.collectAsState().value
    if (selectedProduct != null) {
        ProductDetailDialog(
            product = selectedProduct,
            isBengali = isBn,
            onDismiss = { viewModel.closeProductDetails() },
            onAddToCart = { qty -> viewModel.addToCart(selectedProduct, qty) },
            onBuyNow = { qty -> viewModel.buyNow(selectedProduct) }
        )
    }

    val isCartOpen = viewModel.isCartOpen.collectAsState().value
    if (isCartOpen) {
        CartSheet(
            viewModel = viewModel,
            onDismiss = { viewModel.closeCart() },
            onProceedCheckout = { viewModel.openCheckout() }
        )
    }

    val isCheckoutOpen = viewModel.isCheckoutOpen.collectAsState().value
    if (isCheckoutOpen) {
        CheckoutDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.closeCheckout() }
        )
    }

    val successOrder = viewModel.successOrder.collectAsState().value
    if (successOrder != null) {
        OrderSuccessDialog(
            order = successOrder,
            isBengali = isBn,
            onTrackOrder = { orderId -> viewModel.trackSpecificOrder(orderId) },
            onContinueShopping = { viewModel.dismissSuccessOrder() }
        )
    }
}

@Composable
fun CategorySelectorRow(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val isBn = viewModel.isBengali.collectAsState().value
    val selectedCat = viewModel.selectedCategory.collectAsState().value
    val categories = viewModel.categories

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { cat ->
            val isSelected = selectedCat.equals(cat.id, ignoreCase = true)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) Navy900 else Slate100,
                modifier = Modifier
                    .testTag("cat_pill_${cat.id}")
                    .clickable { viewModel.selectCategory(cat.id) }
            ) {
                Text(
                    text = if (isBn) cat.nameBn else cat.name,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Gold400 else Navy900,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isBn = viewModel.isBengali.collectAsState().value
    val products = viewModel.filteredProducts.collectAsState().value
    val wishlist = viewModel.wishlist.collectAsState().value
    val settings = viewModel.settings.collectAsState().value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        // Hero Banner Carousel
        item(span = { GridItemSpan(2) }) {
            Column {
                HeroBannerSlider(banners = viewModel.banners, viewModel = viewModel)
                Spacer(modifier = Modifier.height(14.dp))
                PromoCards(isBengali = isBn)
                Spacer(modifier = Modifier.height(14.dp))
                CategorySelectorRow(viewModel = viewModel)
            }
        }

        // Section Title: Featured / Trending
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBn) "হট ডিলস ও ট্রেন্ডিং কালেকশন" else "Hot Deals & Trending Items",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = if (isBn) "১০০% অরিজিনাল ও দ্রুত ডেলিভারি" else "100% Genuine & Fast Shipping",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                TextButton(
                    onClick = { viewModel.navigateTo(ShopScreen.CATALOG) },
                    colors = ButtonDefaults.textButtonColors(contentColor = Navy900)
                ) {
                    Text(if (isBn) "সব দেখুন" else "View All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }

        // Product Cards Grid
        items(products, key = { it.id }) { product ->
            ProductCard(
                product = product,
                isBengali = isBn,
                isWishlisted = wishlist.contains(product.id),
                onProductClick = { viewModel.openProductDetails(product) },
                onAddToCart = { viewModel.addToCart(product, 1) },
                onBuyNow = { viewModel.buyNow(product) },
                onToggleWishlist = { viewModel.toggleWishlist(product.id) }
            )
        }

        // Footer Support Bar
        item(span = { GridItemSpan(2) }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Navy950),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "AMINUR BD SHOP",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = Gold400,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = if (isBn) "বাংলাদেশের সবচেয়ে বিশ্বস্ত প্রিমিয়াম লাইফস্টাইল শপ" else "Bangladesh's Most Trusted Premium Lifestyle Shop",
                        fontSize = 11.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${settings.hotlinePhone}"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Navy800, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp), tint = Gold400)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isBn) "কল করুন" else "Call Helpline", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                val url = "https://wa.me/${settings.whatsappNumber.replace("+", "").replace(" ", "").replace("-", "")}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isBn) "হোয়াটসঅ্যাপ" else "WhatsApp", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "© 2026 Aminur BD Shop. All Rights Reserved.",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun CatalogScreen(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val isBn = viewModel.isBengali.collectAsState().value
    val products = viewModel.filteredProducts.collectAsState().value
    val wishlist = viewModel.wishlist.collectAsState().value

    Column(modifier = modifier.fillMaxSize()) {
        CategorySelectorRow(viewModel = viewModel)

        // Result Count Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isBn) "মোট ${products.size} টি প্রোডাক্ট পাওয়া গেছে" else "${products.size} Products Found",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isBn) "কোনো প্রোডাক্ট খুঁজে পাওয়া যায়নি!" else "No products found!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBn) "অন্য কি-ওয়ার্ড দিয়ে সার্চ করুন।" else "Try searching with different keywords.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        isBengali = isBn,
                        isWishlisted = wishlist.contains(product.id),
                        onProductClick = { viewModel.openProductDetails(product) },
                        onAddToCart = { viewModel.addToCart(product, 1) },
                        onBuyNow = { viewModel.buyNow(product) },
                        onToggleWishlist = { viewModel.toggleWishlist(product.id) }
                    )
                }
            }
        }
    }
}
