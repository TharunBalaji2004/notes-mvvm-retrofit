package com.tharunbalaji.mvvm_notes_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tharunbalaji.mvvm_notes_app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}