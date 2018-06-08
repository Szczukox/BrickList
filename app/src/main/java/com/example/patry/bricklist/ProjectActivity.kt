package com.example.patry.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import kotlinx.android.synthetic.main.activity_project.*

class ProjectActivity : AppCompatActivity() {

    var name = ""
    var id = 0
    var inventoryParts = mutableListOf<InventoryPart>()
    var dataBaseHelper : DataBaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        val extras = intent.extras
        name = extras.getString("name")
        id = extras.getInt("id")
        nazwaProjektuTextView.text = name
        dataBaseHelper = DataBaseHelper(applicationContext)

        inventoryParts = dataBaseHelper!!.getInventoryParts(id)
        itemListView.adapter = InventoryPartListViewAdapter(applicationContext, R.layout.inventory_part_view, inventoryParts)
    }

    fun export(v : View) {

    }

    fun archive(v : View) {
        dataBaseHelper!!.updateInventoryActive(id)
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.PROJECT_REQUEST_CODE)
    }

    fun back(v : View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.PROJECT_REQUEST_CODE)
    }
}
