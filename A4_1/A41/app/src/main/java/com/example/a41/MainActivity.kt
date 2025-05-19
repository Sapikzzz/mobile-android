package com.example.a41

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.apply

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("Received Intent:" + intent?.action)
        Toast.makeText(context, intent?.action, Toast.LENGTH_SHORT).show()
    }
}

class MainActivity : AppCompatActivity() {

    private val receiver: BroadcastReceiver = MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(receiver, IntentFilter().apply {
            addAction("android.intent.action.BATTERY_LOW")
            addAction("android.intent.action.BATTERY_OKAY")
            addAction("android.intent.action.ACTION_POWER_CONNECTED")
            addAction("android.intent.action.ACTION_POWER_DISCONNECTED")
        })
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}