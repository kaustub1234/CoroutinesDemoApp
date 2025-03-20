package com.example.democouroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.TreeSet
import kotlin.math.log
import kotlin.system.measureTimeMillis

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

        btn.setOnClickListener {
            setNewText("Clicked!")

            CoroutineScope(Dispatchers.IO).launch {
                fakeApiRequest()
            }
        }

    }

    private suspend fun fakeApiRequest() {
        /*val startTime = System.currentTimeMillis();
        val parentJob = CoroutineScope(Dispatchers.IO).launch {
            val job1 = launch {
                val time1 = measureTimeMillis {
                    Log.d(
                        TAG,
                        "fakeApiRequest: launching job1 in Thread ${Thread.currentThread().name}"
                    )
                    val result1 = getResultFromApi()
                    setTextOnMainThread("Got $result1")
                }
                Log.d(TAG, "fakeApiRequest: completed job1 in $time1 ms.")
            }

            val job2 = launch {
                val time2 = measureTimeMillis {
                    Log.d(
                        TAG,
                        "fakeApiRequest: launching job2 in Thread ${Thread.currentThread().name}"
                    )
                    val result2 = getResult2FromApi()
                    setTextOnMainThread("Got $result2")
                }
                Log.d(TAG, "fakeApiRequest: completed job2 in $time2 ms.")
            }
        }

        parentJob.invokeOnCompletion {
            Log.d(TAG, "fakeApiRequest: total elapsed time ${System.currentTimeMillis()-startTime}")
        }*/

        CoroutineScope(Dispatchers.IO).launch{
            val executionTime = measureTimeMillis {
                val result1 : Deferred<String> = async{
                    Log.d(
                        TAG,
                        "fakeApiRequest: launching job1 in Thread ${Thread.currentThread().name}"
                    )
                    getResultFromApi()
                }

                val result2 : Deferred<String> = async{
                    Log.d(
                        TAG,
                        "fakeApiRequest: launching job1 in Thread ${Thread.currentThread().name}"
                    )
                    getResult2FromApi()
                }

                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
            }
            Log.d(
                TAG,
                "fakeApiRequest: total time elapsed ${executionTime}"
            )

        }
    }

    private fun setNewText(input: String) {
        val newText = counterText.text.toString() + "\n$input"
        counterText.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Dispatchers.Main)
        {
            setNewText(input)
        }
    }

    private suspend fun getResultFromApi(): String {
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1700)
        return "Result #2"
    }

    private suspend fun logThread(methodName: String) {
        Log.d(TAG, "$methodName -> ${Thread.currentThread().name}");
    }
}