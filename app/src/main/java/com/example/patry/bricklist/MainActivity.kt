package com.example.patry.bricklist

import android.database.SQLException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val DBHelper = DataBaseHelper(this)
        try {

            DBHelper.createDataBase()

        } catch (ioe: IOException) {

            throw Error("Unable to create database")
        }
        try {

            DBHelper.openDataBase()

        } catch (sqle: SQLException) {

            throw sqle

        }

    }
}