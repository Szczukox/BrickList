package com.example.patry.bricklist

import android.content.Intent
import android.database.SQLException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var inventories = mutableListOf<Inventory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataBaseHelper = DataBaseHelper(applicationContext)
        try {

            dataBaseHelper.createDataBase()

        } catch (ioe: IOException) {

            throw Error("Unable to create database")
        }

        inventories = dataBaseHelper.getInventories()

        //projektyListView.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, in)

    }

    fun addProject(v: View) {
        val intent = Intent(applicationContext, AddProjectActivity::class.java)
        startActivityForResult(intent, Const.ADD_PROJECT_REQUEST_CODE)
    }

    fun settings(v: View) {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivityForResult(intent, Const.SETTINGS_REQUEST_CODE)
    }
}