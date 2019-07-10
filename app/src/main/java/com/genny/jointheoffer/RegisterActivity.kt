package com.genny.jointheoffer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.*
import kotlinx.android.synthetic.main.activity_inserimento.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button_register.setOnClickListener {
            val username = register_username.text.toString()
            val email = register_email.text.toString()
            val password = register_password.text.toString()

            Toast.makeText(this,username+email+password, LENGTH_LONG).show()

            var db : SQLiteDatabase = openOrCreateDatabase("prova", Context.MODE_PRIVATE,null)
            val ROW1 = ("INSERT INTO " + "users" + " VALUES(" + "'" + username + "'" + ", " + "'" + email + "'" + ", " + "'" + password + "'" + ")")
            db.execSQL(ROW1)

            Toast.makeText(this,"Grazie per esserti registrato!",Toast.LENGTH_LONG).show()
        }
    }
}
