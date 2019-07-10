package com.genny.jointheoffer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MyListAdapter(var cnt:Context,var resource:Int, var items:List<Item>)
    :ArrayAdapter<Item>(cnt,resource,items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(cnt)

        val view: View = layoutInflater.inflate(resource,null)

        val prodotto: TextView = view.findViewById(R.id.title_view)
        val description: TextView = view.findViewById(R.id.descrizione_view)
        val proprietario : TextView = view.findViewById(R.id.proprietario_view)
        val prezzo : TextView = view.findViewById(R.id.prezzo_view)
        val image : ImageView = view.findViewById(R.id.image_view)

        val item: Item = items[position]

        prodotto.text = item.prodotto
        description.text = item.descrizione
        proprietario.text = item.proprietario
        prezzo.text = item.prezzo

        var images:String = prodotto.text.toString()
        var imgID = images.replace(' ','_').toLowerCase()
        var resid = cnt.resources.getIdentifier(imgID ,"drawable", "com.genny.jointheoffer")
        image.setImageResource(resid)


        return view
    }
}