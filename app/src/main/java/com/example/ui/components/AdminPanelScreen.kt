package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.ui.ShopViewModel
import com.example.ui.theme.CodGreen
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Navy950
import com.example.ui.theme.Slate100
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminPanelScreen(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val isBn = viewModel.isBengali.value
    val isAuthenticated = viewModel.isAdminAuthenticated.value
    val passcode = viewModel.adminPasscodeInput.value
    val error = viewModel.adminError.value
    val orders = viewModel.orders.value
    val products = viewModel.filteredProducts.value
    val settings = viewModel.settings.value

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Orders, 1: Products, 2: Analytics & Settings
    var showAddProductDialog by remember { mutableStateOf(false) }

    if (!isAuthenticated) {
        // Admin PIN Login Screen
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth(0.92f)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Navy900, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Gold400,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isBn) "অ্যাডমিন প্যানেল লগইন" else "Admin Portal Access",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )

                    Text(
                        text = if (isBn) "অ্যাডমিন ইমেইল: ${settings.adminEmail}" else "Admin: ${settings.adminEmail}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (error != null) {
                        Surface(
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = error,
                                color = Color(0xFFB91C1C),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = passcode,
                        onValueChange = { viewModel.updateAdminPasscode(it) },
                        placeholder = { Text(if (isBn) "পাসকোড লিখুন (ডিফল্ট: 1234)" else "Passcode (Default: 1234)", fontSize = 12.sp) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_passcode_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.loginAdmin() },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy900, contentColor = Gold500),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("admin_login_submit_btn")
                    ) {
                        Text(if (isBn) "লগইন করুন" else "Access Dashboard", fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = if (isBn) "টিপ: ডিফল্ট পাসকোড '1234'" else "Tip: Default PIN is '1234'",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }
        return
    }

    // Authenticated Admin Dashboard
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate100)
    ) {
        // Admin Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy950)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isBn) "অ্যাডমিন ড্যাশবোর্ড" else "Aminur BD Admin",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = settings.adminEmail,
                    fontSize = 11.sp,
                    color = Gold400
                )
            }

            Button(
                onClick = { viewModel.logoutAdmin() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isBn) "লগআউট" else "Logout", fontSize = 11.sp)
            }
        }

        // Quick KPI Metric Cards
        val totalRevenue = orders.filter { it.status != OrderStatus.CANCELLED }.sumOf { it.total }
        val pendingCount = orders.count { it.status == OrderStatus.PENDING }
        val deliveredCount = orders.count { it.status == OrderStatus.DELIVERED }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminKpiCard(
                title = if (isBn) "মোট রেভিনিউ" else "Total Revenue",
                value = "৳${totalRevenue.toInt()}",
                color = Gold500,
                modifier = Modifier.weight(1.2f)
            )
            AdminKpiCard(
                title = if (isBn) "মোট অর্ডার" else "Orders",
                value = "${orders.size}",
                color = Navy900,
                modifier = Modifier.weight(0.9f)
            )
            AdminKpiCard(
                title = if (isBn) "পেন্ডিং" else "Pending",
                value = "$pendingCount",
                color = Color(0xFFEA580C),
                modifier = Modifier.weight(0.9f)
            )
        }

        // Tabs: Orders | Products | Settings
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Navy900
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(if (isBn) "অর্ডার তালিকা (${orders.size})" else "Orders (${orders.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(if (isBn) "পণ্য ম্যানেজমেন্ট" else "Products", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text(if (isBn) "স্টোর সেটিংস" else "Settings", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Orders Management List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(orders) { ord ->
                        AdminOrderCard(
                            order = ord,
                            isBengali = isBn,
                            onUpdateStatus = { newStatus -> viewModel.updateOrderStatus(ord.id, newStatus) }
                        )
                    }
                }
            }

            1 -> {
                // Products Management
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBn) "পণ্য তালিকা (${products.size} টি)" else "Product Catalog (${products.size})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { showAddProductDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Navy900, contentColor = Gold500),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isBn) "নতুন পণ্য যোগ" else "Add Product", fontSize = 11.sp)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { prod ->
                            AdminProductCard(
                                product = prod,
                                isBengali = isBn,
                                onUpdateStock = { delta -> viewModel.updateProductStock(prod.id, (prod.stock + delta).coerceAtLeast(0)) },
                                onUpdatePrice = { newPrice -> viewModel.updateProductPrice(prod.id, newPrice) }
                            )
                        }
                    }
                }
            }

            2 -> {
                // Store Settings & Gateways
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(if (isBn) "পেমেন্ট গেটওয়ে সেটিংস" else "Payment Gateways", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy900)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("bKash Personal: ${settings.bkashNumber}", fontSize = 12.sp)
                            Text("Nagad Personal: ${settings.nagadNumber}", fontSize = 12.sp)
                            Text(if (isBn) "ক্যাশ অন ডেলিভারি: সক্রিয়" else "Cash on Delivery: Active", fontSize = 12.sp, color = CodGreen, fontWeight = FontWeight.Bold)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(if (isBn) "ডেলিভারি ফি সেটিংস" else "Delivery Settings", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy900)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Inside Dhaka Fee: ৳${settings.insideDhakaFee.toInt()}", fontSize = 12.sp)
                            Text("Outside Dhaka Fee: ৳${settings.outsideDhakaFee.toInt()}", fontSize = 12.sp)
                            Text("Free Delivery Minimum: ৳${settings.freeDeliveryThreshold.toInt()}", fontSize = 12.sp, color = CodGreen, fontWeight = FontWeight.Bold)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(if (isBn) "অ্যাক্টিভ প্রোমো কোডস" else "Active Promo Codes", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy900)
                            Spacer(modifier = Modifier.height(8.dp))
                            settings.promoCodes.forEach { (code, discount) ->
                                Text("• $code -> ${if (discount < 1.0) "${(discount * 100).toInt()}% OFF" else "৳${discount.toInt()} OFF"}", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Product Modal
    if (showAddProductDialog) {
        AddProductDialog(
            isBengali = isBn,
            onDismiss = { showAddProductDialog = false },
            onAdd = { newProd ->
                viewModel.addNewProduct(newProd)
                showAddProductDialog = false
            }
        )
    }
}

@Composable
fun AdminKpiCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Black, color = color, maxLines = 1)
        }
    }
}

@Composable
fun AdminOrderCard(
    order: Order,
    isBengali: Boolean,
    onUpdateStatus: (OrderStatus) -> Unit
) {
    var expandedStatusMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${order.id}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Navy900
                )

                // Status Dropdown
                Box {
                    Surface(
                        color = when (order.status) {
                            OrderStatus.DELIVERED -> Color(0xFFDCFCE7)
                            OrderStatus.CANCELLED -> Color(0xFFFEE2E2)
                            else -> Color(0xFFFEF3C7)
                        },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.clickable { expandedStatusMenu = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isBengali) order.status.titleBn else order.status.titleEn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }

                    DropdownMenu(
                        expanded = expandedStatusMenu,
                        onDismissRequest = { expandedStatusMenu = false }
                    ) {
                        OrderStatus.entries.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(if (isBengali) st.titleBn else st.titleEn, fontSize = 12.sp) },
                                onClick = {
                                    onUpdateStatus(st)
                                    expandedStatusMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(order.createdAt))
            Text(text = "$dateStr • ${order.paymentMethod.labelBn}", fontSize = 11.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${order.customerName} • 📞 ${order.phone}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Navy900
            )
            Text(
                text = "${order.address}, ${order.district}",
                fontSize = 11.sp,
                color = Color.DarkGray
            )

            if (order.transactionId.isNotEmpty()) {
                Text(
                    text = "TrxID: ${order.transactionId}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold500
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Slate100)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.items.size} টি পণ্য • ${order.items.joinToString { it.productNameBn }.take(30)}...",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    text = "মোট: ৳${order.total.toInt()}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy950
                )
            }
        }
    }
}

@Composable
fun AdminProductCard(
    product: Product,
    isBengali: Boolean,
    onUpdateStock: (Int) -> Unit,
    onUpdatePrice: (Double) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .background(Slate100, RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isBengali) product.nameBn else product.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text("৳${product.price.toInt()} (Stock: ${product.stock})", fontSize = 11.sp, color = Color.Gray)
            }

            // Quick Stock Buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onUpdateStock(-1) }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(14.dp))
                }
                Text("${product.stock}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { onUpdateStock(1) }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun AddProductDialog(
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onAdd: (Product) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var nameBn by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var originalPriceStr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("smartwatch") }
    var stockStr by remember { mutableStateOf("20") }
    var image by remember { mutableStateOf("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isBengali) "নতুন পণ্য যোগ করুন" else "Add New Product") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nameBn,
                    onValueChange = { nameBn = it },
                    label = { Text("পণ্যের নাম (বাংলা)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name (English)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("মূল্য (৳)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = originalPriceStr,
                    onValueChange = { originalPriceStr = it },
                    label = { Text("পূর্বের মূল্য (৳)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = stockStr,
                    onValueChange = { stockStr = it },
                    label = { Text("স্টক পরিমাণ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceStr.toDoubleOrNull() ?: 1000.0
                    val op = originalPriceStr.toDoubleOrNull() ?: (p * 1.3)
                    val st = stockStr.toIntOrNull() ?: 10
                    val newProduct = Product(
                        id = "prod-${System.currentTimeMillis() % 10000}",
                        name = name.ifEmpty { "New Product" },
                        nameBn = nameBn.ifEmpty { "নতুন পণ্য" },
                        description = "High quality luxury product directly from Aminur BD Shop.",
                        descriptionBn = "আমিনুর বিডি শপ এর প্রিমিয়াম পণ্য। ১০০% জেনুইন কোয়ালিটি।",
                        price = p,
                        originalPrice = op,
                        discountPercent = if (op > p) (((op - p) / op) * 100).toInt() else 0,
                        category = category,
                        categoryBn = "স্মার্টওয়াচ ও টেক",
                        image = image,
                        stock = st,
                        rating = 5.0,
                        reviewsCount = 1,
                        badge = "নতুন"
                    )
                    onAdd(newProduct)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Navy900, contentColor = Gold500)
            ) {
                Text(if (isBengali) "যোগ করুন" else "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isBengali) "বাতিল" else "Cancel")
            }
        }
    )
}
