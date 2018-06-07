package com.example.patry.bricklist

class Inventory {
    var id : Int = 0
    var name : String = ""
    var active : Int = 1
    var lastAccessed : Long = 0

    constructor(id: Int,name: String,active:Int,lastAccessed:Long)
    {
        this.id=id
        this.name=name
        this.active=active
        this.lastAccessed=lastAccessed
    }

    constructor(name: String,active:Int,lastAccessed:Long)
    {
        this.name=name
        this.active=active
        this.lastAccessed=lastAccessed
    }
}