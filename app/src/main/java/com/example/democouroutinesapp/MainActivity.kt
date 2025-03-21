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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.TreeSet
import kotlin.math.log
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var counter: Int = 0;
    private lateinit var btn: Button;
    private lateinit var counterText: TextView
    private val TAG = javaClass.simpleName
    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.button)
        counterText = findViewById(R.id.textView)

        btn.setOnClickListener {
            main()
        }
    }

    private fun main() {

        CoroutineScope(Dispatchers.Main).launch {
            val result1 = getResult()
            Log.d(TAG, "result1: $result1");

            val result2 = getResult()
            Log.d(TAG, "result2: $result1");

            val result3 = getResult()
            Log.d(TAG, "result2: $result3");

            val result4 = getResult()
            Log.d(TAG, "result2: $result4");

            val result5 = getResult()
            Log.d(TAG, "result2: $result5");
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            //as we are using runblocking here it will block all other coroutines
            //Working on main Thread, thus a runblocking code blocks all the coroutine
            //on which our runblock is running
            runBlocking {
                Log.d(TAG, "main: ${Thread.currentThread().name}")
                delay(4000)
                Log.d(TAG, "main: ${Thread.currentThread().name}")
            }
        }

    }

    private suspend fun getResult():Int{
        delay(1000)
        return Random.nextInt(0, 100)
    }

    private suspend fun logThread(methodName: String) {
        Log.d(TAG, "$methodName -> ${Thread.currentThread().name}");
    }
}