package com.sbdevs.firestorequeries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    private lateinit var queryWithJavaButton: Button
    private lateinit var queryWithKotlinButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        queryWithJavaButton = findViewById(R.id.button_java)
        queryWithKotlinButton = findViewById(R.id.button_Kotlin)

        queryWithJavaButton.setOnClickListener {
            val javaActivityIntent = Intent(this,QueryWithJavaActivity::class.java)
            startActivity(javaActivityIntent)
        }


        queryWithKotlinButton.setOnClickListener {
            val kotlinActivityIntent = Intent(this,QueryWithKotlinActivity::class.java)
            startActivity(kotlinActivityIntent)
        }

    }
}