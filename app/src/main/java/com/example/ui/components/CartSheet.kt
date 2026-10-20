package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CartItem
import com.example.model.DeliveryArea
import com.example.ui.ShopViewModel
import com.example.ui.theme.CodGreen
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Navy950
import com.example.ui.theme.Slate100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartSheet(
    viewModel: ShopViewModel,
    onDismiss: () -> Unit,
    onProceedCheckout: () -> Unit
) {
    val isBn = viewModel.isBengali.value
    val cartItems = viewModel.cartItems.value
    val appliedCoupon = viewModel.appliedCoupon.value
    val settings = viewModel.settings.value

    var couponInput by remember { mutableStateOf("") }
    var selectedDeliveryArea by remember { mutableStateOf(DeliveryArea.INSIDE_DHAKA) }

    val subtotal = cartItems.sumOf { it.totalPrice }
    val isFreeDelivery = subtotal >= settings.freeDeliveryThreshold
    val deliveryFee = if (isFreeDelivery) 0.0 else selectedDeliveryArea.fee

    var discount = 0.0
    appliedCoupon?.let { code ->
        val rule = settings.promoCodes[code]
        if (rule != null) {
            discount = if (rule < 1.0) subtotal * rule else rule
        }
    }
    val total = (subtotal + deliveryFee - discount).coerceAtLeast(0.0)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        modifier = Modifier.testTag("cart_sheet_container")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 16.dp)
        ) {
            // Cart Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Navy900
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) "আপনার শপিং কার্ট (${cartItems.size})" else "Your Shopping Cart (${cartItems.size})",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                }

                if (cartItems.isNotEmpty()) {
                    TextButton(
                        onClick = { viewModel.repository.clearCart() },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text(if (isBn) "সব মুছুন" else "Clear All", fontSize = 12.sp)
                    }
                }
            }

            HorizontalDivider(color = Slate100)

            if (cartItems.isEmpty()) {
                // Empty Cart State
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Slate100, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RemoveShoppingCart,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isBn) "আপনার কার্ট বর্তমানে খালি!" else "Your Cart is Empty!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBn) "কালেকশন থেকে আপনার পছন্দের প্রোডাক্ট যোগ করুন।" else "Add products from our collection to begin.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Navy900, contentColor = Gold500),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isBn) "শপিং শুরু করুন" else "Start Shopping")
                    }
                }
            } else {
                // Items & Delivery progress
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Free delivery banner / progress
                    item {
                        Surface(
                            color = if (isFreeDelivery) Color(0xFFDCFCE7) else Slate100,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isFreeDelivery) Icons.Default.CheckCircle else Icons.Default.LocalShipping,
                                    contentDescription = null,
                                    tint = if (isFreeDelivery) CodGreen else Navy800,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isFreeDelivery) {
                                        if (isBn) "অভিনন্দন! আপনি ফ্রি ডেলিভারি পেয়েছেন!" else "Congrats! You got Free Delivery!"
                                    } else {
                                        val remaining = (settings.freeDeliveryThreshold - subtotal).toInt()
                                        if (isBn) "আর ৳$remaining টাকার অর্ডার করলে ডেলিভারি ফ্রি!" else "Add ৳$remaining more for Free Delivery!"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isFreeDelivery) Color(0xFF166534) else Navy900
                                )
                            }
                        }
                    }

                    // Cart Items list
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            isBengali = isBn,
                            onIncrease = { viewModel.updateCartQuantity(item.product.id, 1) },
                            onDecrease = { viewModel.updateCartQuantity(item.product.id, -1) },
                            onRemove = { viewModel.removeFromCart(item.product.id) }
                        )
                    }

                    // Delivery Area Selector
                    item {
                        Text(
                            text = if (isBn) "ডেলিভারি লোকেশন:" else "Delivery Location:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Inside Dhaka
                            DeliveryAreaCard(
                                title = if (isBn) "ঢাকা সিটির ভেতরে" else "Inside Dhaka",
                                fee = if (isFreeDelivery) (if (isBn) "ফ্রি" else "Free") else "৳${settings.insideDhakaFee.toInt()}",
                                isSelected = selectedDeliveryArea == DeliveryArea.INSIDE_DHAKA,
                                onSelect = { selectedDeliveryArea = DeliveryArea.INSIDE_DHAKA },
                                modifier = Modifier.weight(1f)
                            )

                            // Outside Dhaka
                            DeliveryAreaCard(
                                title = if (isBn) "ঢাকা সিটির বাইরে" else "Outside Dhaka",
                                fee = if (isFreeDelivery) (if (isBn) "ফ্রি" else "Free") else "৳${settings.outsideDhakaFee.toInt()}",
                                isSelected = selectedDeliveryArea == DeliveryArea.OUTSIDE_DHAKA,
                                onSelect = { selectedDeliveryArea = DeliveryArea.OUTSIDE_DHAKA },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Coupon Input
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = couponInput,
                                onValueChange = { couponInput = it },
                                placeholder = {
                                    Text(
                                        if (isBn) "কুপন কোড (যেমন: AMINUR10)" else "Coupon code (e.g. AMINUR10)",
                                        fontSize = 12.sp
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Gold500,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (couponInput.isNotBlank()) {
                                        viewModel.applyCoupon(couponInput)
                                        couponInput = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Navy800),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text(if (isBn) "প্রয়োগ" else "Apply", fontSize = 12.sp)
                            }
                        }

                        // Applied Coupon Chip
                        if (appliedCoupon != null) {
                            Surface(
                                color = Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(top = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${if (isBn) "কুপন সক্রিয়:" else "Active Coupon:"} $appliedCoupon",
                                        color = Color(0xFF92400E),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Coupon",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clickable { viewModel.removeCoupon() }
                                    )
                                }
                            }
                        }
                    }
                }

                // Summary & Checkout Button
                Surface(
                    color = Slate100,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(if (isBn) "সাবটোটাল" else "Subtotal", fontSize = 12.sp, color = Color.Gray)
                            Text("৳${subtotal.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(if (isBn) "ডেলিভারি চার্জ" else "Delivery Fee", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = if (deliveryFee == 0.0) (if (isBn) "ফ্রি (৳০)" else "Free") else "৳${deliveryFee.toInt()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (deliveryFee == 0.0) CodGreen else Navy900
                            )
                        }
                        if (discount > 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (isBn) "কুপন ডিসকাউন্ট" else "Coupon Discount", fontSize = 12.sp, color = Color.Red)
                                Text("-৳${discount.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(if (isBn) "সর্বমোট" else "Total Amount", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy900)
                            Text("৳${total.toInt()}", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Navy950)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onProceedCheckout,
                    colors = ButtonDefaults.buttonColors(containerColor = Navy950, contentColor = Gold500),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("proceed_checkout_button")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBn) "চেকআউট করুন (৳${total.toInt()})" else "Proceed to Checkout (৳${total.toInt()})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    isBengali: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.product.image,
                contentDescription = item.product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Slate100)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isBengali) item.product.nameBn else item.product.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Navy900,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "৳${item.product.price.toInt()} x ${item.quantity} = ৳${item.totalPrice.toInt()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold500
                )
            }

            // Stepper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Slate100, RoundedCornerShape(6.dp))
                    .padding(2.dp)
            ) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Navy900, modifier = Modifier.size(14.dp))
                }
                Text(
                    text = "${item.quantity}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                IconButton(onClick = onIncrease, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = Navy900, modifier = Modifier.size(14.dp))
                }
            }

            IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun DeliveryAreaCard(
    title: String,
    fee: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Navy900 else Slate100
        ),
        modifier = modifier.clickable { onSelect() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else Navy900
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = fee,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Gold400 else Color.Gray
            )
        }
    }
}
