package com.translized.translizedandroidota

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.translized.translized_ota.Translized

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(Translized.wrapContext(newBase))
    }
}