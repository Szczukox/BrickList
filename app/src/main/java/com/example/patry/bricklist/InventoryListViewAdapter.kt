package com.example.patry.bricklist


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class InventoryListAdapter : ArrayAdapter<Inventory> {

    constructor(context: Context, resource: Int, objects: MutableList<Inventory>) : super(context, resource, objects)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var v = convertView

        if (v == null) {
            val vi: LayoutInflater = LayoutInflater.from(context)
            v = vi.inflate(R.layout.inventory_view, null)
        }

        val p = getItem(position)

        if (p != null) {
            val tytulProjektu = v!!.findViewById<View>(R.id.tytulProjektuTextView) as TextView
            tytulProjektu.text = p.name
        }

        return v
    }
}