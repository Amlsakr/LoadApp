package com.example.loadapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout

class DetailActivity : AppCompatActivity() {

    lateinit var fileNameTV : TextView
    lateinit var fileStatus : TextView
    lateinit var backBT :Button
    lateinit var motionLayout : MotionLayout
    companion object {
        const val FILE_NAME = "file_name"
        const val FILE_URL = "file_url"
        const val FILE_STATUS = "file_status"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        fileNameTV = findViewById(R.id.file_name)
        fileStatus = findViewById(R.id.file_status)
        backBT = findViewById(R.id.go_back)
        motionLayout = findViewById(R.id.motion_layout)
     //   setSupportActionBar(toolbar)

        fileNameTV.text =
            String.format(getString(R.string.file_name), intent.getStringExtra(FILE_NAME))

        fileStatus.text =
            String.format(getString(R.string.file_status), intent.getStringExtra(FILE_STATUS))

    backBT.setOnClickListener {
        motionLayout.transitionToEnd{
            startActivity(Intent(this , MainActivity::class.java))
            finish()
        }

    }
    }

}