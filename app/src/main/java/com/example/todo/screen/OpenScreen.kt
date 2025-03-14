package com.example.todo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OpenScreen(modifier: Modifier = Modifier,
               onGetStarted: ()->Unit){
    Box(modifier = modifier.fillMaxSize().background(Color(0xFF6EA68E))
    ){
        Column(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Take control",
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 50.sp,
                modifier = Modifier.padding(10.dp)
            )
            Text(text = "of your",
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 50.sp,
                modifier = Modifier.padding(10.dp)
            )
            Text(text = "schedule",
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 50.sp,
                modifier = Modifier.padding(10.dp)
            )
            Button(
                onClick = onGetStarted,
                modifier = Modifier.padding(10.dp,20.dp).size(350.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFD5FFA4))
            ){
                Text("Get Started", color = Color.Black, fontSize = 20.sp)

            }
        }
    }
}

@Composable
@Preview
fun OpenScreenPreview(){
    OpenScreen(onGetStarted = {})
}