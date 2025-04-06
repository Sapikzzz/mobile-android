package com.example.app3

import android.app.ComponentCaller
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        imageView = findViewById(R.id.imageView);

        val callIntent: Intent = Uri.parse("tel:5511234").let { number ->
            Intent(Intent.ACTION_DIAL, number)
        }
        val systemDisplaySetting: Intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)

        val location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California")
        val mapIntent = Intent(Intent.ACTION_VIEW, location)

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        btn1.setOnClickListener {
            try {
                startActivity(systemDisplaySetting);
            } catch (e: ActivityNotFoundException){
                e.printStackTrace();
            }
        }

        btn2.setOnClickListener {
            try {
                startActivity(mapIntent);
            } catch (e: ActivityNotFoundException){
                e.printStackTrace();
            }
        }

        btn3.setOnClickListener {
            try {
                startActivityForResult(takePictureIntent, 1)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace();
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            if (imageBitmap != null) {
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }

}