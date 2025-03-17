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
    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.button)
        counterText = findViewById(R.id.textView)

        btn.setOnClickListener {
            //Coroutine scopes: IO,main,Default
            CoroutineScope(Dispatchers.IO).launch {
                fakeApiReq()
            }
        }

    }

    private fun setNewText(input:String)
    {
        val newText = counterText.text.toString()+"\n$input"
        counterText.text = newText
    }

    suspend fun setTextOnMainThread(input:String)
    {
        withContext(Dispatchers.Main)
        {
            setNewText(input);
        }
    }

    private suspend fun fakeApiReq() {
        val result1 = getResultFromApi()
        Log.d(TAG, "fakeApiReq: $result1")
        setTextOnMainThread(result1)

        val result2 = getResultFromApi2()
        setTextOnMainThread(result2)
    }

    private suspend fun getResultFromApi(): String {
        logThread("getResultFromApi")
        /**
         * Thread.sleep in java make an whole thread to sleep
         * whereas delay function in coroutines makes an job/coroutine to go for sleep
         * Thread can host many jobs/coroutines
         * */

        delay(1000)
        return RESULT_1;
    }

    private suspend fun getResultFromApi2(): String {
        logThread("getResultFromApi2")
        /**
         * Thread.sleep in java make an whole thread to sleep
         * whereas delay function in coroutines makes an job/coroutine to go for sleep
         * Thread can host many jobs/coroutines
         * */

        delay(1000)
        return RESULT_2;
    }

    private suspend fun logThread(methodName: String) {
        Log.d(TAG, "$methodName -> ${Thread.currentThread().name}");
    }
}