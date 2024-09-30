package com.example.camara

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun launchCameraActivity(view: View?) {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    fun launchQRActivity(view: View?) {
        val intent = Intent(this, QRActivity::class.java)
        startActivity(intent)
    }
}