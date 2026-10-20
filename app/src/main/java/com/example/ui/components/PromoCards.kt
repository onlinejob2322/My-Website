package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Gold500
import com.example.ui.theme.Navy800
import com.example.ui.theme.Slate100

data class PromoItem(
    val icon: ImageVector,
    val titleBn: String,
    val titleEn: String,
    val subBn: String,
    val subEn: String
)

@Composable
fun PromoCards(
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        PromoItem(
            icon = Icons.Default.LocalShipping,
            titleBn = "ফ্রি ডেলিভারি",
            titleEn = "Free Delivery",
            subBn = "৳২৫০০+ অর্ডারে",
            subEn = "Orders over ৳2500"
        ),
        PromoItem(
            icon = Icons.Default.Payments,
            titleBn = "ক্যাশ অন ডেলিভারি",
            titleEn = "Cash on Delivery",
            subBn = "সারা বাংলাদেশে",
            subEn = "Across Bangladesh"
        ),
        PromoItem(
            icon = Icons.Default.VerifiedUser,
            titleBn = "১০০% অরিজিনাল",
            titleEn = "100% Genuine",
            subBn = "প্রিমিয়াম কোয়ালিটি",
            subEn = "Guaranteed Quality"
        ),
        PromoItem(
            icon = Icons.Default.HeadsetMic,
            titleBn = "লাইভ সাপোর্ট",
            titleEn = "24/7 Support",
            subBn = "হটলাইন ও হোয়াটসঅ্যাপ",
            subEn = "Call & WhatsApp"
        )
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Navy800, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = Gold500,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isBengali) item.titleBn else item.titleEn,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy800,
                        maxLines = 1
                    )
                    Text(
                        text = if (isBengali) item.subBn else item.subEn,
                        fontSize = 9.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
