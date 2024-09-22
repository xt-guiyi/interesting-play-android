package com.xtguiyi.loveLife.ui.me

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.xtguiyi.loveLife.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MePage() {
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Header(innerPadding.calculateTopPadding())
            // 使用weight填充剩余高度
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(255, 255, 255))
                    .fillMaxWidth(),
            ) {
                Body()
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Header(top: Dp) {
    Box() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = Color(4, 181, 120))
                .padding(start = 18.dp, top = top.plus(14.dp), end = 18.dp, bottom = 68.dp)
        ) {
            // 背景图片
            GlideImage(
                model = "https://images.cubox.pro/iw3rni/file/2024061800331149633/IMG_0021.JPG", // 替换为你的图片资源
                contentDescription = "头像",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape)
            )
            Text(
                "xt_guiyi",
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 10.dp, bottom = 2.dp)
            )
            Text(
                "ip地址：广东", fontSize = 14.sp,
                color = Color.White
            )
            Text(
                "简介：我的灵魂深处有一缕阳光，里面飘着些尘埃",
                fontSize = 12.sp,
                maxLines = 2,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 8.dp, top = 2.dp, bottom = 12.dp)
                    .fillMaxWidth(),
            )
            Row(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "99",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                    Text(
                        "关注",
                        color = Color.White,
                        fontSize = 14.sp,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "26",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                    Text(
                        "动态",
                        color = Color.White,
                        fontSize = 14.sp,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "101",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                    Text(
                        "粉丝",
                        color = Color.White,
                        fontSize = 14.sp,
                    )
                }
            }
        }
        val compositionResult = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wave))
        LottieAnimation(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .wrapContentHeight()
                .offset(y = 2.dp),
            contentScale = ContentScale.Crop,
            composition = compositionResult.value,
            iterations = LottieConstants.IterateForever,
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Body() {
    FlowRow(
        maxItemsInEachRow = 4,
        modifier = Modifier
            .padding(start = 12.dp, top = 24.dp, end = 12.dp, bottom = 12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.collect_online),
                modifier = Modifier.size(24.dp),
                contentDescription = "我的收藏"
            )
            Text("我的收藏")
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.customer_services),
                modifier = Modifier.size(24.dp),
                contentDescription = "联系客服"
            )
            Text("联系客服")
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.book),
                modifier = Modifier.size(24.dp),
                contentDescription = "电子书"
            )
            Text("电子书")
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.qr),
                modifier = Modifier.size(24.dp),
                contentDescription = "扫一扫"
            )
            Text("扫一扫")
        }
        Column(
            modifier = Modifier.fillMaxWidth(0.25f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.setting),
                modifier = Modifier.size(24.dp),
                contentDescription = "设置"
            )
            Text("设置")
        }
    }
}
