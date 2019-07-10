package com.genny.jointheoffer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast

class MyproductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myproducts)

        //Shared preference per sessione
        var shared: SharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE)
        var utente = shared.getString("username"," ")
        if (utente == " ") {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)//activity login
        }

        //Database
        var db : SQLiteDatabase = openOrCreateDatabase("prova", Context.MODE_PRIVATE,null)
        //LISTVIEW

        //generazione list da query
        var listview = findViewById<ListView>(R.id.listView_prodotti)
        var list = mutableListOf<Item>()

        var raw = "select * from products where proprietario = " + "'" + utente + "'"
        var cursor: Cursor = db.rawQuery(raw,null)
        while (cursor.moveToNext()) {
            var nomep = cursor.getString(0)
            var descrp = cursor.getString(1)
            var userp = cursor.getString(2)
            var prezzop = cursor.getString(5)

            list.add(Item(nomep,descrp,userp,prezzop))
        }

        //adapter
        listview.adapter = MyListAdapter(this, R.layout.row, list)

        listview.setOnItemClickListener { parent, view, position, id ->
            val nome = list[position].prodotto
            val intent = Intent(this, ProdottoActivity::class.java)
            intent.putExtra("nome", nome)
            startActivity(intent)
        }



    }
}
