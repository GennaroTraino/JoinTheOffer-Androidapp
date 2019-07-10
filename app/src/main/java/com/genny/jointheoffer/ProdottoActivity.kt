package com.genny.jointheoffer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_prodotto.*

class ProdottoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prodotto)

        val bundle: Bundle? = intent.extras
        val prodotto:String = bundle!!.getString("nome").toString()

        var shared: SharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE)
        var utente = shared.getString("username"," ")



        var db : SQLiteDatabase = openOrCreateDatabase("prova", Context.MODE_PRIVATE,null)
        var row = "SELECT * FROM products WHERE prodotto = '" + prodotto +"'"
        var cursor: Cursor = db.rawQuery(row,null)
        cursor.moveToFirst()
        nome_prodotto.text = cursor.getString(0).toString()
        descrizione_prodotto.text = cursor.getString(1).toString()
        join_prodotto.text = cursor.getInt(3).toString()
        quantita_prodotto.text = cursor.getInt(4).toString()
        prezzo_prodotto.text = cursor.getString(5).toString()

        var n_join = cursor.getInt(3)
        var quantita = cursor.getInt(4)


        var imgID:String = prodotto.replace(' ','_').toLowerCase()
        var resid = applicationContext.applicationContext.resources.getIdentifier(imgID ,"drawable", "com.genny.jointheoffer")
        imageView_prodotto.setImageResource(resid)

        button_join_product.setOnClickListener {
            /*    CONTROLLA SE HA GIA JOINATO    */
            val row2 = "select * from joiner where joiner = '"+ utente +"'"
            var cursor2: Cursor = db.rawQuery(row2,null)
            while(cursor2.moveToNext()){
                if(cursor2.getString(0).toString() == prodotto){
                    Toast.makeText(this,"Hai gia Joinato",Toast.LENGTH_LONG).show()
                    break
                }
            }
            /*   JOINA   */
            n_join = n_join + 1

            var riga = "update products set prenotazioni = " + n_join +" where prodotto = '" + prodotto + "'"
            db.execSQL(riga)
            join_prodotto.text = n_join.toString()
            riga = "insert into joiner values ('" + prodotto +"','"+ utente + "')"
            db.execSQL(riga)

            if(n_join == quantita) {
                prezzo_prodotto.text = "VENDITA PARTITA!"
            }
        }

        button_left_product.setOnClickListener {
            var row2 = "delete from joiner where joiner = '"+ utente +"'" + " and prodotto = '" + prodotto +"'"
            db.execSQL(row2)
            n_join = n_join - 1
            var row5:String = "update products set prenotazioni = " + n_join + " where prodotto = '" + prodotto + "'"
            db.execSQL(row5)
            join_prodotto.text = n_join.toString()

        }

    }
}
