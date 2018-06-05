package com.example.patry.bricklist

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import java.net.URL
import kotlinx.android.synthetic.main.activity_add_project.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException


class AddProjectActivity : AppCompatActivity() {

    val MAIN_URL = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
    val DOWNLOAD_PATH = "/data/data/com.example.patry.bricklist/XML/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
    }


    fun downloadXML() {
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
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        })
        thread.start()
    }

    fun add(v: View) {
        downloadXML()
    }

    fun cancel(v: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, Const.MAIN_ACTIVITY_REQUEST_CODE)
    }
}

