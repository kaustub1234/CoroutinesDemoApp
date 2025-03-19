package com.example.democouroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.TreeSet
import java.util.concurrent.CancellationException
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000
    private lateinit var job: CompletableJob
    private lateinit var btn: Button
    private lateinit var counterText: TextView
    private lateinit var progressBar: LinearProgressIndicator
    private val TAG = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.button)
        counterText = findViewById(R.id.textView)
        progressBar = findViewById(R.id.progressBar)


        btn.setOnClickListener {
            if (!::job.isInitialized) {
                initJob()
            }
            progressBar.startJobOrCancel(job)
        }

    }

    fun LinearProgressIndicator.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            Log.d(TAG, "$job is already active. Cancelling.....")
            resetJob()
        } else {
            btn.text = "Cancel job #1"
            CoroutineScope(Dispatchers.IO + job).launch {
                Log.d(TAG, "coroutines $this is activated with job ${job}")

                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    withContext(Dispatchers.Main)
                    {
                        this@startJobOrCancel.progress = i
                    }
                }

                updateJobCompleteTextView("Job is completed")
            }
        }
    }

    private fun updateJobCompleteTextView(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            counterText.text = text
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting job"))
        }
        initJob()
    }

    private fun initJob() {
        btn.text = "Start a Job #1"
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrEmpty())
                    msg = "Unknown cancellation is called"
                Log.d(TAG, "initJob: $job was cancelled. Reason: $msg")
            }
        }

        progressBar.max = PROGRESS_MAX
        progressBar.progress = PROGRESS_START
    }

    private suspend fun logThread(methodName: String) {
        Log.d(TAG, "$methodName -> ${Thread.currentThread().name}");
    }

    fun showToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT)
    }
}