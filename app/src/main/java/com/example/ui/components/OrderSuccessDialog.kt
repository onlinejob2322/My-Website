package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Order
import com.example.ui.theme.CodGreen
import com.example.ui.theme.Gold500
import com.example.ui.theme.Navy900
import com.example.ui.theme.Navy950
import com.example.ui.theme.Slate100

@Composable
fun OrderSuccessDialog(
    order: Order,
    isBengali: Boolean,
    onTrackOrder: (String) -> Unit,
    onContinueShopping: () -> Unit
) {
    val context = LocalContext.current

    fun copyOrderId() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Order ID", order.id)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, if (isBengali) "অর্ডার আইডি কপি করা হয়েছে!" else "Order ID copied!", Toast.LENGTH_SHORT).show()
    }

    Dialog(
        onDismissRequest = onContinueShopping,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Badge
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(CodGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isBengali) "অর্ডার সফলভাবে সম্পন্ন হয়েছে!" else "Order Placed Successfully!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy950
                )

                Text(
                    text = if (isBengali) "আমিনুর বিডি শপ-এ কেনাকাটার জন্য ধন্যবাদ।" else "Thank you for shopping with Aminur BD Shop.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Order ID Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(if (isBengali) "আপনার অর্ডার ট্র্যাকিং আইডি:" else "Order Tracking ID:", fontSize = 11.sp, color = Color.Gray)
                            Text(order.id, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Navy900)
                        }
                        IconButton(onClick = { copyOrderId() }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Order ID", tint = Navy900)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Simulated SMS Confirmation Preview
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Sms, contentDescription = null, tint = CodGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "কনফার্মেশন এসএমএস প্রিভিউ:" else "SMS Confirmation Preview:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isBengali) {
                                "[Aminur BD Shop] প্রিয় ${order.customerName}, আপনার অর্ডার #${order.id} গ্রহণ করা হয়েছে। মোট মূল্য: ৳${order.total.toInt()} (${order.paymentMethod.labelBn})। আমাদের ডেলিভারি টিম শীঘ্রই আপনার সাথে যোগাযোগ করবে।"
                            } else {
                                "[Aminur BD Shop] Dear ${order.customerName}, order #${order.id} received. Total: ৳${order.total.toInt()} (${order.paymentMethod.labelEn}). Our courier will contact you soon."
                            },
                            fontSize = 11.sp,
                            color = Color(0xFF14532D),
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Summary details
                Surface(
                    color = Slate100,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (isBengali) "গ্রাহকের নাম" else "Customer", fontSize = 12.sp, color = Color.Gray)
                            Text(order.customerName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (isBengali) "মোবাইল নম্বর" else "Phone", fontSize = 12.sp, color = Color.Gray)
                            Text(order.phone, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (isBengali) "ডেলিভারি ঠিকানা" else "Address", fontSize = 12.sp, color = Color.Gray)
                            Text("${order.address}, ${order.district}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (isBengali) "পেমেন্ট মেথড" else "Payment", fontSize = 12.sp, color = Color.Gray)
                            Text(if (isBengali) order.paymentMethod.labelBn else order.paymentMethod.labelEn, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (isBengali) "মোট প্রদেয়" else "Total Paid/Payable", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("৳${order.total.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Navy900)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Button(
                    onClick = { onTrackOrder(order.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Navy900, contentColor = Gold500),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("track_order_success_btn")
                ) {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isBengali) "লাইভ অর্ডার ট্র্যাক করুন" else "Track Order Live", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onContinueShopping,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text(if (isBengali) "আরও কেনাকাটা করুন" else "Continue Shopping", fontSize = 13.sp, color = Navy900)
                }
            }
        }
    }
}
