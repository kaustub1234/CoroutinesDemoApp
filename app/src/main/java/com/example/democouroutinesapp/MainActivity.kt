package com.example.democouroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.TreeSet
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private var counter: Int = 0;
    private lateinit var btn: Button;
    private lateinit var counterText: TextView
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.button)
        counterText = findViewById(R.id.textView)

        main()

        btn.setOnClickListener {
            counterText.text = (counter++).toString()
        }

    }

    private suspend fun doNetworkRequest()
    {
        Log.d(TAG, "Starting network request!")
        delay(3000)
        Log.d(TAG, "Finished network request!")
    }

    fun main()
    {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d(TAG, "Current thread: ${Thread.currentThread().name}")
            for (i in 1..100_000)
            {
                doNetworkRequest()
            }
        }
    }


    private suspend fun logThread(methodName: String) {
        Log.d(TAG, "$methodName -> ${Thread.currentThread().name}");
    }
}