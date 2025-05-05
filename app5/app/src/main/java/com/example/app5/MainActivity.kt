package com.example.app5

import android.R.attr.onClick
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.app5.ui.theme.App5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App5Theme {
                var enabledButton1 = remember { mutableStateOf(true) }
                var enabledButton2 = remember { mutableStateOf(false) }
                var enabledButton3 = remember { mutableStateOf(false) }

                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { enabledButton1.value = false; enabledButton2.value = true; enabledButton3.value = false }, enabled = enabledButton1.value) {Text(text = "Button 1")}
                    Button(onClick = { enabledButton1.value = false; enabledButton2.value = false; enabledButton3.value = true }, enabled = enabledButton2.value) {Text("Button 2")}
                    Button(onClick = { enabledButton1.value = true; enabledButton2.value = false; enabledButton3.value = false }, enabled = enabledButton3.value) {Text("Button 3")}
                }
            }
        }
    }
}
