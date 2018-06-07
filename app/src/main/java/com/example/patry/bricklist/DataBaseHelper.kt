package com.example.patry.bricklist

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.database.SQLException
import java.io.FileOutputStream
import java.io.IOException


class DataBaseHelper
/**
 * Constructor
 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
 * @param context
 */
(private val myContext: Context?) : SQLiteOpenHelper(myContext, DB_NAME, null, 1) {

    private var myDataBase: SQLiteDatabase? = null

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    @Throws(IOException::class)
    fun createDataBase() {

        val dbExist = checkDataBase()
        var db_read : SQLiteDatabase?=null

        if (dbExist) {
            //do nothing - database already exis
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            db_read = this.readableDatabase
            db_read.close()

            try {

                copyDataBase()

            } catch (e: IOException) {

                throw Error("Error copying database")

            }

        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private fun checkDataBase(): Boolean {

        var checkDB: SQLiteDatabase? = null

        try {
            val myPath = DB_PATH + DB_NAME
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)

        } catch (e: SQLiteException) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close()

        }

        return if (checkDB != null) true else false
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    @Throws(IOException::class)
    private fun copyDataBase() {

        //Open your local db as the input stream
        val myInput = myContext!!.getAssets().open(DB_NAME)

        // Path to the just created empty db
        val outFileName = DB_PATH + DB_NAME

        //Open the empty db as the output stream
        val myOutput = FileOutputStream(outFileName)

        //transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length: Int
        length = myInput.read(buffer)
        while (length  > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }

        //Close the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()

    }

    @Throws(SQLException::class)
    fun openDataBase() {

        //Open the database
        val myPath = DB_PATH + DB_NAME
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)

    }

    @Synchronized
    override fun close() {

        if (myDataBase != null)
            myDataBase!!.close()

        super.close()

    }

    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun insertInventory(inventory: Inventory) {
        val values = ContentValues()
        values.put("`Name`", inventory.name)
        values.put("`Active`", inventory.active)
        values.put("`LastAccessed`", inventory.lastAccessed)
        val writableDatabase = this.writableDatabase
        writableDatabase.insert("`Inventories`", null, values)
        writableDatabase.close()
    }

    fun insertInventoryPart(inventoryPart: InventoryPart) {
        val values = ContentValues()
        values.put("InventoryID", inventoryPart.inventoryID)
        values.put("TypeID", inventoryPart.typeID)
        values.put("ItemID", inventoryPart.itemID)
        values.put("QuantityInSet", inventoryPart.quantityInSet)
        values.put("QuantityInStore", inventoryPart.quantityInStore)
        values.put("ColorID", inventoryPart.colorID)
        values.put("Extra", inventoryPart.extra)
        val writableDatabase = this.writableDatabase
        writableDatabase.insert("InventoriesParts", null, values)
        writableDatabase.close()
    }

    fun getNewInventoryID() : Int {
        var newInventoryID = 0
        val sqlQuery = "SELECT id FROM Inventories ORDER BY id DESC"
        val writableDatabase = this.writableDatabase
        val cursor = writableDatabase.rawQuery(sqlQuery, null)
        if (cursor.moveToFirst()) {
            newInventoryID = Integer.parseInt(cursor.getString(0))
        }
        cursor.close()
        return newInventoryID
    }

    fun getTypeID(code : String) : Int {
        var typeID = 0
        val sqlQuery = "SELECT id FROM ItemTypes WHERE  Code='$code'"
        val writableDatabase = this.writableDatabase
        val cursor = writableDatabase.rawQuery(sqlQuery, null)
        if (cursor.moveToFirst()) {
            typeID = Integer.parseInt(cursor.getString(0))
        }
        cursor.close()
        return typeID
    }

    fun getItemID(code : String) : Int {
        var itemID = 0
        val sqlQuery = "SELECT id FROM Parts WHERE  Code='$code'"
        val writableDatabase = this.writableDatabase
        val cursor = writableDatabase.rawQuery(sqlQuery, null)
        if (cursor.moveToFirst()) {
            itemID = Integer.parseInt(cursor.getString(0))
        }
        cursor.close()
        return itemID
    }

    fun getColorID(code : String) : Int {
        var colorID = 0
        val sqlQuery = "SELECT id FROM Colors WHERE  Code=$code"
        val writableDatabase = this.writableDatabase
        val cursor = writableDatabase.rawQuery(sqlQuery, null)
        if (cursor.moveToFirst()) {
            colorID = Integer.parseInt(cursor.getString(0))
        }
        cursor.close()
        return colorID
    }

    fun getInventories() : MutableList<Inventory>{
        val inventories = mutableListOf<Inventory>()
        val sqlQuery = "SELECT * FROM Inventories ORDER BY LastAccessed DESC"
        val writableDatabase = this.writableDatabase
        val cursor = writableDatabase.rawQuery(sqlQuery, null)
        cursor.moveToFirst()
        while (cursor.moveToNext()) {
            val inventory = Inventory(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3))
            inventories.add(inventory)
        }
        cursor.close()
        return inventories
    }

    fun getInventoryParts(inventoryID : Int) : MutableList<InventoryPart> {
        val inventoriesParts = mutableListOf<InventoryPart>()
        val sqlQuery = "SELECT * FROM InventoriesParts WHERE InventoryID = $inventoryID"
        val writableDatabase = this.writableDatabase
        val cursor = writableDatabase.rawQuery(sqlQuery, null)
        cursor.moveToFirst()
        while (cursor.moveToNext()) {
            val inventoriesPart = InventoryPart(cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getInt(7))
            inventoriesParts.add(inventoriesPart)
        }
        cursor.close()
        return inventoriesParts
    }

    companion object {

        //The Android's default system path of your application database.
        private val DB_PATH = "/data/data/com.example.patry.bricklist/databases/"

        private val DB_NAME = "BrickList.db"
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}