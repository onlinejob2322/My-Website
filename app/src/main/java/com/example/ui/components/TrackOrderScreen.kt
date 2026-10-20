package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
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
fun TrackOrderScreen(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val isBn = viewModel.isBengali.value
    val input = viewModel.trackingInput.value
    val order = viewModel.trackedOrder.value
    val searched = viewModel.trackingSearched.value

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Banner Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = Gold400,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) "লাইভ অর্ডার ট্র্যাকিং" else "Live Order Tracking",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isBn)
                        "আপনার অর্ডার আইডি (যেমন: ABD-10842) অথবা মোবাইল নম্বর দিয়ে পার্সেলের বর্তমান অবস্থা জানুন।"
                    else
                        "Enter your Order ID (e.g. ABD-10842) or mobile number to track live status.",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Input + Search Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { viewModel.updateTrackingInput(it) },
                        placeholder = {
                            Text(
                                if (isBn) "অর্ডার আইডি / ফোন নম্বর" else "Order ID or Phone",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Navy800,
                            unfocusedContainerColor = Navy800,
                            focusedBorderColor = Gold400,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("track_order_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { viewModel.searchOrderTracking() },
                        colors = ButtonDefaults.buttonColors(containerColor = Gold500, contentColor = Navy950),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("track_search_button")
                    ) {
                        Text(if (isBn) "ট্র্যাক" else "Track", fontWeight = FontWeight.Bold)
                    }
                }

                // Sample click suggestion
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBn) "ডেমো ট্র্যাকিং:" else "Demo tracking:",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ABD-10842",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gold400,
                        modifier = Modifier.clickable {
                            viewModel.updateTrackingInput("ABD-10842")
                            viewModel.searchOrderTracking()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results Section
        if (searched) {
            if (order != null) {
                OrderTrackingDetailsCard(order = order, isBengali = isBn)
            } else {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isBn) "কোনো অর্ডার পাওয়া যায়নি!" else "No Order Found!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBn)
                                "দয়া করে সঠিক অর্ডার আইডি বা মোবাইল নম্বর দিয়ে পুনরায় চেষ্টা করুন।"
                            else
                                "Please check the Order ID or phone number and try again.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderTrackingDetailsCard(
    order: Order,
    isBengali: Boolean
) {
    val steps = listOf(
        OrderStatus.PENDING,
        OrderStatus.CONFIRMED,
        OrderStatus.PROCESSING,
        OrderStatus.SHIPPED,
        OrderStatus.OUT_FOR_DELIVERY,
        OrderStatus.DELIVERED
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBengali) "অর্ডার #${order.id}" else "Order #${order.id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Navy900
                    )
                    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(order.createdAt))
                    Text(dateStr, fontSize = 11.sp, color = Color.Gray)
                }

                Surface(
                    color = when (order.status) {
                        OrderStatus.DELIVERED -> Color(0xFFDCFCE7)
                        OrderStatus.CANCELLED -> Color(0xFFFEE2E2)
                        else -> Color(0xFFFEF3C7)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isBengali) order.status.titleBn else order.status.titleEn,
                        color = when (order.status) {
                            OrderStatus.DELIVERED -> Color(0xFF166534)
                            OrderStatus.CANCELLED -> Color(0xFF991B1B)
                            else -> Color(0xFF92400E)
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multi-step Progress Visualizer
            Text(
                text = if (isBengali) "ডেলিভারি অগ্রগতি:" else "Delivery Milestones:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900
            )

            Spacer(modifier = Modifier.height(10.dp))

            steps.forEachIndexed { index, step ->
                val isCompleted = order.status.stepIndex >= step.stepIndex
                val isCurrent = order.status == step

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circle indicator
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCurrent -> Gold500
                                    isCompleted -> CodGreen
                                    else -> Slate100
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        } else {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray))
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Text(
                            text = if (isBengali) step.titleBn else step.titleEn,
                            fontSize = 13.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCompleted) Navy900 else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Slate100)
            Spacer(modifier = Modifier.height(12.dp))

            // Courier Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(if (isBengali) "কুরিয়ার পার্টনার" else "Courier Partner", fontSize = 11.sp, color = Color.Gray)
                    Text(order.courierName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy900)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(if (isBengali) "কুরিয়ার কনসাইনমেন্ট" else "Consignment ID", fontSize = 11.sp, color = Color.Gray)
                    Text(order.courierTrackingCode.ifEmpty { "STDF-998412" }, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gold500)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Delivery info
            Surface(
                color = Slate100,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = if (isBengali) "গ্রাহক: ${order.customerName} (${order.phone})" else "Customer: ${order.customerName} (${order.phone})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (isBengali) "ঠিকানা: ${order.address}, ${order.district}" else "Address: ${order.address}, ${order.district}",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = if (isBengali) "মোট প্রদেয়: ৳${order.total.toInt()} (${order.paymentMethod.labelBn})" else "Total: ৳${order.total.toInt()} (${order.paymentMethod.labelEn})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
