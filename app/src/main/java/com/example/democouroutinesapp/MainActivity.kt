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
import kotlinx.coroutines.GlobalScope
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
        main()
    }
    
    fun main()
    {
        /**GlobalScope does not wait for its
        parent job it will execute independently
        from the current thread*/
        val startTime = System.currentTimeMillis()
        Log.d(TAG, "main: Starting parent job....")
        val parentJob = CoroutineScope(Dispatchers.Main).launch { 
            GlobalScope.launch { 
                work(1)
            }
            
            GlobalScope.launch { 
                work(2)
            }
        }
        
        parentJob.invokeOnCompletion { 
            if(it!=null)
            {
                Log.d(TAG, "main: Job was canceled after ${System.currentTimeMillis()-startTime} ms.")
            }
        }
    }

    private suspend fun work(i: Int) {
        delay(3000)
        Log.d(TAG, "work: $i done: ${Thread.currentThread().name}")
    }


    private suspend fun logThread(methodName: String) {
        Log.d(TAG, "$methodName -> ${Thread.currentThread().name}");
    }
}