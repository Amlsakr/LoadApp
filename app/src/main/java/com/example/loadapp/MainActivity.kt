package com.example.loadapp

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import org.apache.commons.io.FilenameUtils

enum class DownloadStatus(val value:Int) {
    SUCCESS(1),FAIL(0)
}
class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager:DownloadManager
    private lateinit var downloadStatus :DownloadStatus
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var fileUrl = ""
    private var fileName = ""

    lateinit var custom_button : LoadingButton
    lateinit var radioGroup: RadioGroup


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        custom_button = findViewById(R.id.custom_button)
        radioGroup = findViewById(R.id.radio_group)
        notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
        NotificationUtills.createChannel(CHANNEL_ID ,  CHANNEL_NAME ,notificationManager)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            Thread{
                download()
            }.start()

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                val query = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
                if (query.moveToFirst()) {
                    val statusIndex = query.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    when (query.getInt(statusIndex)) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            downloadStatus = DownloadStatus.SUCCESS
                        }
                        DownloadManager.STATUS_FAILED -> {
                            downloadStatus = DownloadStatus.FAIL
                        }
                    }

                    NotificationUtills.buildNotification(
                        this@MainActivity,
                        resources.getString(R.string.notification_description),
                        fileUrl,
                        fileName,
                        downloadStatus.toString()
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun download() {
        if (radioGroup.checkedRadioButtonId == -1 ){
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Please Select One Option to Start Dwonloading",
                    Toast.LENGTH_LONG
                ).show()
            }

            custom_button.changeButtonState(ButtonState.Completed)
        }else {
            fileUrl = findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
            fileName = FilenameUtils.getName(fileUrl)
            val request =
                DownloadManager.Request(Uri.parse(URL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

             downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        const val CHANNEL_ID = "channelId"
        const val CHANNEL_NAME = "channelName"
    }
}