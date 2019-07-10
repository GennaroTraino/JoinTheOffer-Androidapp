package com.genny.jointheoffer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //DATABASE
        var db : SQLiteDatabase = openOrCreateDatabase("prova", Context.MODE_PRIVATE,null)

        //SHARED
        var shared: SharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE)

        if(shared.getString("username"," ") != " " ) { // se gia loggato
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        register_inloginbutton.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        loginbutton.setOnClickListener {
            var cond:Int = 0
            var username = nomeedit.text.toString()
            var password = passwordedit.text.toString()

            var cursor: Cursor = db.rawQuery("SELECT * FROM users",null)
            while (cursor.moveToNext()) {
                var nome:String = cursor.getString(0)
                var password_corretta:String = cursor.getString(2)

                // condition login
                if (username.toUpperCase() == nome.toUpperCase() && password.toUpperCase() == password_corretta.toUpperCase()){

                    cond = 1;
                    var intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("username",username)
                    var editor = shared.edit()
                    editor.putString("username",username)
                    editor.commit()

                    startActivity(intent)
                    finish()
                }

                if(cond == 0) {
                    Toast.makeText(this,"Username o Password non Corretti",Toast.LENGTH_LONG).show()
                }


            }

        }

    }
}
