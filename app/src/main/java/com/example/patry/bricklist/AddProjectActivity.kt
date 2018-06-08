package com.example.patry.bricklist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import java.net.URL
import kotlinx.android.synthetic.main.activity_add_project.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.*
import java.net.MalformedURLException
import javax.xml.parsers.DocumentBuilderFactory


class AddProjectActivity : AppCompatActivity() {

    var MAIN_URL = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
    val DOWNLOAD_PATH = "/data/data/com.example.patry.bricklist/XML/"
    var dataBaseHelper : DataBaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
        val extras = intent.extras
        MAIN_URL = extras.getString("url")
        dataBaseHelper = DataBaseHelper(applicationContext)
    }


    private fun downloadXML() : Boolean {
        var valid = true
        val thread = Thread(Runnable {
            try {
                val url = URL(MAIN_URL + numberInventoryEditText.text.toString() + ".xml")
                val conexion = url.openConnection()
                conexion.connect()
                val lenghtOfFile = conexion.contentLength
                val input = url.openStream()
                val testDirectory = File(DOWNLOAD_PATH)
                testDirectory.mkdirs()
                val fos = FileOutputStream(testDirectory.toString() + "/" + numberInventoryEditText.text.toString() + ".xml")
                val data = ByteArray(1024)
                var count = input.read(data)
                var total: Long = 0
                while (count != -1) {
                    total += count.toLong()
                    fos.write(data, 0, count)
                    count = input.read(data)
                }
                input.close()
                fos.close()
            } catch (e: MalformedURLException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                valid = false
                return@Runnable
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                valid = false
                return@Runnable
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                valid = false
                e.printStackTrace()
                return@Runnable
            }
        })
        thread.start()
        thread.join()
        return valid
    }

    private fun parseXML() {
        val xml = File(DOWNLOAD_PATH + numberInventoryEditText.text.toString() + ".xml")
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val parseXML = documentBuilder.parse(xml)
        parseXML.getDocumentElement().normalize()
        val valueOfItem = parseXML.getElementsByTagName("ITEM")

        for (item in 0 until valueOfItem.length) {
            val node = valueOfItem.item(item)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                if (element.getElementsByTagName("ALTERNATE").item(0).textContent=="N") {
                    val inventoryID = dataBaseHelper!!.getNewInventoryID()
                    val typeID = dataBaseHelper!!.getTypeID(element.getElementsByTagName("ITEMTYPE").item(0).textContent.toString())
                    val itemID = dataBaseHelper!!.getItemID(element.getElementsByTagName("ITEMID").item(0).textContent.toString())
                    val quantityInStore = element.getElementsByTagName("QTY").item(0).textContent.toInt()
                    val colorID = dataBaseHelper!!.getColorID(element.getElementsByTagName("COLOR").item(0).textContent.toString())
                    val extra = element.getElementsByTagName("EXTRA").item(0).textContent.toString()
                    val extraID = if (extra == "N") 0 else 1
                    val inventoryPart = InventoryPart(inventoryID, typeID, itemID, 0, quantityInStore, colorID, extraID)
                    /*println(inventoryID)
                    println(typeID)
                    println(itemID)
                    println(quantityInStore)
                    println(colorID)
                    println(extraID)*/
                    dataBaseHelper!!.insertInventoryPart(inventoryPart)
                }
            }
        }

    }

    fun add(v: View) {
        if (downloadXML()) {
            val newInventory = Inventory(nazwaEditText.text.toString(), 1, System.currentTimeMillis())
            dataBaseHelper!!.insertInventory(newInventory)
            parseXML()

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivityForResult(intent, Const.PROJECT_REQUEST_CODE)

            val toast = Toast.makeText(applicationContext,"Dodano projekt.", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            val toast = Toast.makeText(applicationContext,"Nie znaleziono projektu o podanym numerze.", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun cancel(v: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.MAIN_ACTIVITY_REQUEST_CODE)
    }
}

