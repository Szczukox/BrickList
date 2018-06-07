package com.example.patry.bricklist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
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

        projektyListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, ProjectActivity::class.java)
            intent.putExtra("id", inventories.get(position).id)
            intent.putExtra("name", inventories.get(position).name)
            startActivity(intent)
        }

        inventories = dataBaseHelper.getInventories()
        projektyListView.adapter = InventoryListAdapter(applicationContext, R.layout.inventory_view, inventories)
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