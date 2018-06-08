package com.example.patry.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    var MAIN_URL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val extras = intent.extras
        MAIN_URL = extras.getString("url")
        prefiksEditText.setText(MAIN_URL)
    }

    fun save(v: View) {

        val toast = Toast.makeText(applicationContext,"Zapisano.", Toast.LENGTH_SHORT)
        toast.show()

        val intent = Intent(applicationContext, MainActivity::class.java)
        MAIN_URL = prefiksEditText.text.toString()
        intent.putExtra("url", MAIN_URL)
        startActivityForResult(intent, Const.MAIN_ACTIVITY_REQUEST_CODE)
    }

    fun cancel(v: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.MAIN_ACTIVITY_REQUEST_CODE)
    }
}
