package com.genny.jointheoffer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.database.sqlite.SQLiteDatabase
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_inserimento.*

class ActivityInserimento : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserimento)

        var db : SQLiteDatabase = openOrCreateDatabase("prova", Context.MODE_PRIVATE,null)
        var shared: SharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE)


        buttonInsert.setOnClickListener {
            val nome:String = txtNameItem.text.toString()
            val descrizione:String = txtDescritionItem.text.toString()
            val s_quantita:String = txtCountItem.text.toString()
            val s_prezzo:String = txtPrice.text.toString()
            val proprietario:String = shared.getString("username"," ").toString()


            val quantita:Int = s_quantita.toInt()
            val prezzo:Int = s_prezzo.toInt()

            //TODO inserire proprietario
            //db.execSQL("INSERT INTO products VALUES(nome,descrizione,proprietario,0,quantita,prezzo"))
            //db.execSQL("INSERT INTO products VALUES(?,'Originale iphone nella versione 64GB di memoria','sasybello',4,20,'700 euro','tecnologia');")

            val ROW1 = ("INSERT INTO " + "products" + " Values(" + "'" + nome + "'" + ", " + "'" + descrizione + "'" + ", " + "'" + proprietario + "'" + ", "
                    + "0, " + "'" + quantita + "'" + ", " + "'" + prezzo + " euro','tecnologia')")
            //Toast.makeText(this,ROW1,Toast.LENGTH_LONG).show()
            db.execSQL(ROW1)

            // cambiare activity che manda alla pagina prodotto
            val intent = Intent(this,ProdottoActivity::class.java)
            intent.putExtra("nome",nome)
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finish()
        }
    }
}
