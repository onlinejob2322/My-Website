package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.BannerItem
import com.example.ui.ShopScreen
import com.example.ui.ShopViewModel
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Navy950
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroBannerSlider(
    banners: List<BannerItem>,
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return

    val isBn = viewModel.isBengali.value
    val pagerState = rememberPagerState(pageCount = { banners.size })

    // Auto-advance banner carousel every 5 seconds
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
        ) { page ->
            val banner = banners[page]
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (banner.targetCategory != "all") {
                            viewModel.selectCategory(banner.targetCategory)
                        }
                        viewModel.navigateTo(ShopScreen.CATALOG)
                    }
                    .testTag("banner_item_${banner.id}")
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Image with async load
                    AsyncImage(
                        model = banner.imageUrl,
                        contentDescription = banner.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dark gradient overlay for readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Navy950.copy(alpha = 0.92f),
                                        Navy950.copy(alpha = 0.70f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Text & Call to Action Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = Gold500,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = banner.badge,
                                color = Navy950,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (isBn) banner.titleBn else banner.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isBn) banner.subtitleBn else banner.subtitle,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                maxLines = 2,
                                lineHeight = 15.sp
                            )
                        }

                        Button(
                            onClick = {
                                if (banner.targetCategory != "all") {
                                    viewModel.selectCategory(banner.targetCategory)
                                }
                                viewModel.navigateTo(ShopScreen.CATALOG)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Gold500,
                                contentColor = Navy950
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (isBn) banner.buttonTextBn else banner.buttonText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Indicator dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            banners.indices.forEach { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Gold400 else Color.LightGray.copy(alpha = 0.5f))
                )
            }
        }
    }
}
