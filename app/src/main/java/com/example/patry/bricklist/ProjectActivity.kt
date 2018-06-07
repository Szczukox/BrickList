package com.example.patry.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ProjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
    }

    fun archive(v : View) {

    }

    fun back(v : View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.MAIN_ACTIVITY_REQUEST_CODE)
    }
}
