package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.DeliveryArea
import com.example.model.PaymentMethod
import com.example.ui.ShopViewModel
import com.example.ui.theme.BkashPink
import com.example.ui.theme.CodGreen
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Navy950
import com.example.ui.theme.Slate100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutDialog(
    viewModel: ShopViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isBn = viewModel.isBengali.value
    val cartItems = viewModel.cartItems.value
    val settings = viewModel.settings.value
    val appliedCoupon = viewModel.appliedCoupon.value

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("Dhaka") }
    var deliveryArea by remember { mutableStateOf(DeliveryArea.INSIDE_DHAKA) }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.COD) }
    var transactionId by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val districts = listOf(
        "Dhaka", "Chattogram", "Sylhet", "Rajshahi", "Khulna",
        "Barishal", "Rangpur", "Mymensingh", "Gazipur", "Narayanganj", "Cumilla"
    )
    var districtExpanded by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.totalPrice }
    val isFreeDelivery = subtotal >= settings.freeDeliveryThreshold
    val deliveryFee = if (isFreeDelivery) 0.0 else deliveryArea.fee

    var discount = 0.0
    appliedCoupon?.let { code ->
        val rule = settings.promoCodes[code]
        if (rule != null) {
            discount = if (rule < 1.0) subtotal * rule else rule
        }
    }
    val total = (subtotal + deliveryFee - discount).coerceAtLeast(0.0)

    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, if (isBn) "$text কপি করা হয়েছে!" else "$text copied!", Toast.LENGTH_SHORT).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Navy950)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isBn) "অর্ডার কনফার্মেশন" else "Checkout & Confirm",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isBn) "সঠিক তথ্য দিয়ে অর্ডার সম্পন্ন করুন" else "Please fill your delivery details",
                            fontSize = 11.sp,
                            color = Gold400
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Scrollable Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    if (errorMessage != null) {
                        Surface(
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = Color(0xFFB91C1C),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    // 1. Customer Name
                    Text(
                        text = if (isBn) "আপনার নাম *" else "Full Name *",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; errorMessage = null },
                        placeholder = { Text(if (isBn) "যেমন: তানভীর আহমেদ" else "e.g. Tanvir Ahmed", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp)
                            .testTag("checkout_name_input")
                    )

                    // 2. Mobile Number
                    Text(
                        text = if (isBn) "মোবাইল নম্বর (১১ ডিজিট) *" else "Phone Number (11 Digits) *",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; errorMessage = null },
                        placeholder = { Text("017XXXXXXXX", fontSize = 12.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp)
                            .testTag("checkout_phone_input")
                    )

                    // 3. District Selector
                    Text(
                        text = if (isBn) "জেলা নির্বাচন করুন *" else "District *",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    ExposedDropdownMenuBox(
                        expanded = districtExpanded,
                        onExpandedChange = { districtExpanded = !districtExpanded },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = district,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = districtExpanded,
                            onDismissRequest = { districtExpanded = false }
                        ) {
                            districts.forEach { d ->
                                DropdownMenuItem(
                                    text = { Text(d) },
                                    onClick = {
                                        district = d
                                        deliveryArea = if (d.equals("Dhaka", ignoreCase = true)) {
                                            DeliveryArea.INSIDE_DHAKA
                                        } else {
                                            DeliveryArea.OUTSIDE_DHAKA
                                        }
                                        districtExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // 4. Delivery Address
                    Text(
                        text = if (isBn) "পূর্ণ ডেলিভারি ঠিকানা *" else "Full Delivery Address *",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it; errorMessage = null },
                        placeholder = {
                            Text(
                                if (isBn) "বাড়ি নম্বর, রোড নম্বর, এলাকা, থানা" else "House, Road, Area, Thana",
                                fontSize = 12.sp
                            )
                        },
                        minLines = 2,
                        maxLines = 3,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp)
                            .testTag("checkout_address_input")
                    )

                    // 5. Delivery Location Fee Choice
                    Text(
                        text = if (isBn) "ডেলিভারি এরিয়া" else "Delivery Area",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = if (deliveryArea == DeliveryArea.INSIDE_DHAKA) Navy900 else Slate100,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { deliveryArea = DeliveryArea.INSIDE_DHAKA }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    if (isBn) "ঢাকা সিটি" else "Inside Dhaka",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (deliveryArea == DeliveryArea.INSIDE_DHAKA) Color.White else Navy900
                                )
                                Text(
                                    if (isFreeDelivery) (if (isBn) "ফ্রি" else "Free") else "৳${settings.insideDhakaFee.toInt()}",
                                    fontSize = 11.sp,
                                    color = if (deliveryArea == DeliveryArea.INSIDE_DHAKA) Gold400 else Color.Gray
                                )
                            }
                        }

                        Surface(
                            color = if (deliveryArea == DeliveryArea.OUTSIDE_DHAKA) Navy900 else Slate100,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { deliveryArea = DeliveryArea.OUTSIDE_DHAKA }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    if (isBn) "ঢাকার বাইরে" else "Outside Dhaka",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (deliveryArea == DeliveryArea.OUTSIDE_DHAKA) Color.White else Navy900
                                )
                                Text(
                                    if (isFreeDelivery) (if (isBn) "ফ্রি" else "Free") else "৳${settings.outsideDhakaFee.toInt()}",
                                    fontSize = 11.sp,
                                    color = if (deliveryArea == DeliveryArea.OUTSIDE_DHAKA) Gold400 else Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 6. Payment Methods
                    Text(
                        text = if (isBn) "পেমেন্ট মেথড নির্বাচন করুন *" else "Select Payment Method *",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )

                    // COD Option
                    PaymentOptionCard(
                        title = if (isBn) "ক্যাশ অন ডেলিভারি (COD)" else "Cash on Delivery",
                        sub = if (isBn) "পণ্য হাতে পেয়ে দেখে মূল্য পরিশোধ করুন" else "Pay cash when receiving the product",
                        badgeColor = CodGreen,
                        badgeText = if (isBn) "জনপ্রিয়" else "Popular",
                        isSelected = paymentMethod == PaymentMethod.COD,
                        onSelect = { paymentMethod = PaymentMethod.COD }
                    )

                    // bKash Option
                    PaymentOptionCard(
                        title = if (isBn) "বিকাশ (bKash Personal)" else "bKash (Send Money)",
                        sub = if (isBn) "পার্সোনাল নম্বরে সেন্ড মানি করুন" else "Send Money to personal number",
                        badgeColor = BkashPink,
                        badgeText = "bKash",
                        isSelected = paymentMethod == PaymentMethod.BKASH,
                        onSelect = { paymentMethod = PaymentMethod.BKASH }
                    )

                    // Nagad Option
                    PaymentOptionCard(
                        title = if (isBn) "নগদ (Nagad Personal)" else "Nagad (Send Money)",
                        sub = if (isBn) "পার্সোনাল নম্বরে সেন্ড মানি করুন" else "Send Money to personal number",
                        badgeColor = NagadOrange,
                        badgeText = "Nagad",
                        isSelected = paymentMethod == PaymentMethod.NAGAD,
                        onSelect = { paymentMethod = PaymentMethod.NAGAD }
                    )

                    // If bKash or Nagad selected, show account instructions and TrxID input
                    AnimatedVisibility(visible = paymentMethod == PaymentMethod.BKASH || paymentMethod == PaymentMethod.NAGAD) {
                        val number = if (paymentMethod == PaymentMethod.BKASH) settings.bkashNumber else settings.nagadNumber
                        val brandName = if (paymentMethod == PaymentMethod.BKASH) "বিকাশ" else "নগদ"
                        val brandColor = if (paymentMethod == PaymentMethod.BKASH) BkashPink else NagadOrange

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isBn) "$brandName পার্সোনাল নম্বর:" else "$brandName Personal Number:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Navy900
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = number,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black,
                                        color = brandColor
                                    )
                                    Button(
                                        onClick = { copyToClipboard(number, "$brandName Number") },
                                        colors = ButtonDefaults.buttonColors(containerColor = brandColor),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isBn) "কপি করুন" else "Copy", fontSize = 11.sp)
                                    }
                                }

                                Text(
                                    text = if (isBn)
                                        "উপরের নম্বরে মোট ৳${total.toInt()} টাকা Send Money করে নিচে ট্রানজেকশন আইডি (TrxID) লিখুন।"
                                    else
                                        "Send Money ৳${total.toInt()} to the number above and enter your Transaction ID below.",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 15.sp
                                )

                                OutlinedTextField(
                                    value = transactionId,
                                    onValueChange = { transactionId = it; errorMessage = null },
                                    placeholder = { Text(if (isBn) "ট্রানজেকশন আইডি (যেমন: 9J4K2L...)" else "TrxID (e.g. 9J4K2L...)", fontSize = 12.sp) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .testTag("checkout_trxid_input")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 7. Order Note
                    Text(
                        text = if (isBn) "অর্ডার স্পেশাল নোট (ঐচ্ছিক)" else "Order Note (Optional)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        placeholder = { Text(if (isBn) "কালার/সাইজ বা ডেলিভারির বিশেষ নির্দেশনা" else "Color, size, or special instructions", fontSize = 12.sp) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp)
                    )

                    // 8. Order Cost Summary
                    Surface(
                        color = Slate100,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (isBn) "পণ্য সংখ্যা" else "Total Items", fontSize = 12.sp)
                                Text("${cartItems.sumOf { it.quantity }}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (isBn) "সাবটোটাল" else "Subtotal", fontSize = 12.sp)
                                Text("৳${subtotal.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (isBn) "ডেলিভারি চার্জ" else "Delivery Fee", fontSize = 12.sp)
                                Text(
                                    if (deliveryFee == 0.0) (if (isBn) "ফ্রি (৳০)" else "Free") else "৳${deliveryFee.toInt()}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (deliveryFee == 0.0) CodGreen else Navy900
                                )
                            }
                            if (discount > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(if (isBn) "ডিসকাউন্ট" else "Discount", fontSize = 12.sp, color = Color.Red)
                                    Text("-৳${discount.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (isBn) "সর্বমোট প্রদেয়" else "Total Payable", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("৳${total.toInt()}", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Navy900)
                            }
                        }
                    }
                }

                // Confirm Button
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            // Validations
                            if (name.trim().length < 2) {
                                errorMessage = if (isBn) "দয়া করে আপনার নাম লিখুন" else "Please enter your name"
                                return@Button
                            }
                            if (phone.trim().length < 11) {
                                errorMessage = if (isBn) "সঠিক ১১ ডিজিটের মোবাইল নম্বর দিন (যেমন: 017XXXXXXXX)" else "Please enter a valid 11-digit mobile number"
                                return@Button
                            }
                            if (address.trim().length < 5) {
                                errorMessage = if (isBn) "বিস্তারিত ডেলিভারি ঠিকানা লিখুন" else "Please enter full delivery address"
                                return@Button
                            }
                            if ((paymentMethod == PaymentMethod.BKASH || paymentMethod == PaymentMethod.NAGAD) && transactionId.trim().isEmpty()) {
                                errorMessage = if (isBn) "দয়া করে ট্রানজেকশন আইডি (TrxID) লিখুন" else "Please provide your Transaction ID"
                                return@Button
                            }

                            viewModel.submitOrder(
                                customerName = name.trim(),
                                phone = phone.trim(),
                                address = address.trim(),
                                district = district,
                                deliveryArea = deliveryArea,
                                paymentMethod = paymentMethod,
                                transactionId = transactionId.trim(),
                                note = note.trim()
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy950, contentColor = Gold500),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(50.dp)
                            .testTag("confirm_order_button")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "অর্ডার নিশ্চিত করুন (৳${total.toInt()})" else "Confirm Order (৳${total.toInt()})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOptionCard(
    title: String,
    sub: String,
    badgeColor: Color,
    badgeText: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFF0FDF4) else Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) Navy900 else Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = Navy900)
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = badgeColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
                Text(text = sub, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}
