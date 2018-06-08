package com.example.patry.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_project.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

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
        val itemTypes = ArrayList<String>()
        val itemIds = ArrayList<Int>()
        val colors = ArrayList<Int>()
        val qTy= ArrayList<Int>()

        dataBaseHelper!!.getDataToExportXML(id,itemTypes, itemIds, colors, qTy)

        val documentBuilder : DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document : Document = documentBuilder.newDocument()
        val rootElement : Element =  document.createElement("INVENTORY")

        for ( i in 0 until  itemIds.size-1) {
            val item: Element = document.createElement("ITEM")
            val itemType: Element = document.createElement("ITEMTYPE")
            itemType.appendChild(document.createTextNode(itemTypes[i]))
            item.appendChild(itemType)

            val itemId: Element = document.createElement("ITEMID")
            itemId.appendChild(document.createTextNode(itemIds[i].toString()))
            item.appendChild(itemId)


            val color: Element = document.createElement("COLOR")
            color.appendChild(document.createTextNode(colors[i].toString()))
            item.appendChild(color)

            val qTyFilled: Element = document.createElement("QTYFILLED")
            qTyFilled.appendChild(document.createTextNode(qTy[i].toString()))
            item.appendChild(qTyFilled)
            rootElement.appendChild(item)
        }

        document.appendChild(rootElement)
        val transformer : Transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT,"yes")


        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2")
        val path = this.filesDir
        val outDir = File(path,"Output")
        outDir.mkdir()
        val file = File(outDir, "$name.xml")
        transformer.transform(DOMSource(document), StreamResult(file))

        val toast = Toast.makeText(applicationContext,"Wyeksportowano.", Toast.LENGTH_SHORT)
        toast.show()

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.PROJECT_REQUEST_CODE)
    }

    fun archive(v : View) {
        dataBaseHelper!!.updateInventoryActive(id)
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.PROJECT_REQUEST_CODE)

        val toast = Toast.makeText(applicationContext,"Zarchiwizowano.", Toast.LENGTH_SHORT)
        toast.show()
    }

    fun back(v : View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.PROJECT_REQUEST_CODE)
    }
}
