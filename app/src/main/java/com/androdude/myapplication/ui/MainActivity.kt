package com.androdude.myapplication.ui

import android.content.ClipData
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import com.androdude.myapplication.R

class MainActivity : AppCompatActivity() {
    private lateinit var selectedImageUri : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("TAGS", "BIO")
        cm.setPrimaryClip(clip)
    }



}