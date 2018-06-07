package com.example.patry.bricklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class InventoryPartListViewAdapter : ArrayAdapter<InventoryPart> {

    constructor(context: Context, resource: Int, objects: MutableList<InventoryPart>) : super(context, resource, objects)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var v = convertView

        if (v == null) {
            val vi: LayoutInflater = LayoutInflater.from(context)
            v = vi.inflate(R.layout.inventory_part_view, null)
        }

        val p = getItem(position)

        if (p != null) {
            val nazwaKlocka = v!!.findViewById<View>(R.id.nazwaKlockaTextView) as TextView
            val liczbaKlockow = v.findViewById<View>(R.id.liczbaKlockowTextView) as TextView
            //val dodajButton = v.findViewById<View>(R.id.dodajButton) as Button
            //val minusButton = v.findViewById<View>(R.id.minusButton) as Button

            liczbaKlockow.text = "${p.quantityInSet} / ${p.quantityInStore}"

            /*dodajButton.setOnClickListener {
                if (p.quantityInSet < p.quantityInStore) {
                    p.quantityInSet += 1
                }
            }

            minusButton.setOnClickListener {
                if (p.quantityInSet > 0) {
                    p.quantityInSet -= 1
                }
            }*/
        }

        return v
    }
}