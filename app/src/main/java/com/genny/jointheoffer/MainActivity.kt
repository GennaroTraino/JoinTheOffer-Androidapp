package com.genny.jointheoffer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.ContextMenu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // INIZIO CODICE MENU
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(this,ActivityInserimento::class.java)
            startActivity(intent)
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        // FINE CODICE MENU


        // DATABASE
        var db : SQLiteDatabase = openOrCreateDatabase("prova", Context.MODE_PRIVATE,null)
        //creazioneDatabase(db)


        //Shared preference per sessione
        var shared: SharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE)

        if(shared.getString("username"," ") == " " ) {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)//activity login
        }


        //LISTVIEW

        //generazione list da query
        var listview = findViewById<ListView>(R.id.listView_prodotti)
        var list = mutableListOf<Item>()

        var cursor:Cursor = db.rawQuery("SELECT * FROM products",null)
        while (cursor.moveToNext()) {
            var nomep = cursor.getString(0)
            var descrp = cursor.getString(1)
            var userp = cursor.getString(2)
            var prezzop = cursor.getString(5)

            list.add(Item(nomep,descrp,userp,prezzop))
        }

        //adapter
        listview.adapter = MyListAdapter(this, R.layout.row, list)

        // Onclick
        listview.setOnItemClickListener { parent, view, position, id ->
            val nome = list[position].prodotto
            val intent = Intent(this,ProdottoActivity::class.java)
            intent.putExtra("nome",nome)
            startActivity(intent)
        }

        //menu
        registerForContextMenu(listview)






    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu!!.setHeaderTitle("opzioni")

        menu.add(0,v!!.id,0,"Join")
        menu.add(0,v!!.id,0,"Left")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item!!.title == "Join") {
            Toast.makeText(this,"Hai joinato",Toast.LENGTH_LONG).show()
        } else if (item.title == "Left") {
            Toast.makeText(this,"Hai Lasciato",Toast.LENGTH_LONG).show()
        }

        return super.onContextItemSelected(item)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            //android.os.Process.killProcess(android.os.Process.myPid())
            //System.exit(1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val db:SQLiteDatabase = openOrCreateDatabase("prova", Context.MODE_PRIVATE,null)
                creazioneDatabase(db)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_home -> {
                // home press
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_myproducts -> {
                val intent = Intent(this,MyproductsActivity::class.java)
                startActivity(intent)

            }

            R.id.nav_myjoin -> {
                val intent = Intent(this,myjoinsActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_insertitem -> {
                val intent_insert = Intent(this,ActivityInserimento::class.java)
                startActivity(intent_insert)
            }
            R.id.nav_logout -> {
                var shared: SharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE)
                var editor = shared.edit()
                editor.putString("username"," ")
                editor.commit()
                val intent_logout = Intent(this,LoginActivity::class.java)
                startActivity(intent_logout)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        /*var db:SQLiteDatabase = openOrCreateDatabase("prova", Context.MODE_PRIVATE,null)
        db.execSQL("DELETE FROM users")
        db.execSQL("DELETE FROM products")
        db.execSQL("DELETE FROM joiner")*/

    }

    fun creazioneDatabase(db:SQLiteDatabase) {
        /* Creazioni e inserimenti di tabelle */
        db.execSQL("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, email TEXT, password TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS products (prodotto TEXT PRIMARY KEY, descrizione TEXT, proprietario TEXT, prenotazioni INTEGER, quantita INTEGER, prezzo TEXT, categoria TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS joiner (prodotto TEXT, joiner TEXT)")


        db.execSQL("DELETE FROM users")
        db.execSQL("INSERT INTO users VALUES('pippo42','pippo42@gmail.com','pippo42');")
        db.execSQL("INSERT INTO users VALUES('sasybello','sasybello@gmail.com','sasybello');")
        db.execSQL("INSERT INTO users VALUES('belgatto','belgatto@gmail.com','belgatto');")
        db.execSQL("INSERT INTO users VALUES('utente1','utente1@gmail.com','utente1');")
        db.execSQL("INSERT INTO users VALUES('utente2','utente2@gmail.com','utente2');")
        db.execSQL("INSERT INTO users VALUES('utente3','utente3@gmail.com','utente3');")
        db.execSQL("INSERT INTO users VALUES('utente4','utente4@gmail.com','utente4');")
        db.execSQL("INSERT INTO users VALUES('utente5','utente5@gmail.com','utente5');")
        db.execSQL("INSERT INTO users VALUES('utente6','utente6@gmail.com','utente6');")


        db.execSQL("DELETE FROM products")
        db.execSQL("INSERT INTO products VALUES('IphoneXS 64GB','Originale iphone nella versione 64GB di memoria','sasybello',4,20,'700 euro','tecnologia');")
        db.execSQL("INSERT INTO products VALUES('Surface Pro 6','La versione 6 del super laptop di microsoft, in versione 128 Gb di memoria','utente1',4,5,'1500 euro','tecnologia');")
        db.execSQL("INSERT INTO products VALUES('IphoneXS Max 64GB','Originale iphone nella versione 64GB di memoria','sasybello',0,30,'900 euro','tecnologia');")
        db.execSQL("INSERT INTO products VALUES('Samsung Galaxy S10 64GB','Originale Samsung Galaxy s10 Plus nella versione 64GB di memoria','sasybello',0,30,'700 euro','tecnologia');")
        db.execSQL("INSERT INTO products VALUES('Kant Divano 4 posti','Divano originale Kant 4 posti in velluto color senape','utente4',0,30,'700 euro','casa');")
        db.execSQL("INSERT INTO products VALUES('CZZ Scrivania Pieghevole','Scrivania Pieghevole Computer a Staffa a Forma di U - Semplice Economica per Casa','utente3',4,50,'20 euro','casa');")
        db.execSQL("INSERT INTO products VALUES('Adidas Felpa Originals','Felpa con cappuccio trifoglio iconica con un logo trefoil davanti e al centro','utente3',2,30,'24 euro','abbigliamento');")
        db.execSQL("INSERT INTO products VALUES('Adidas VC 1000 Nere','Scarpe Adidas Sneaker Uomo nere, eleganti, sportive e comode','pippo42',0,30,'30 euro','abbigliamento');")
        db.execSQL("INSERT INTO products VALUES('Nike Air Zoom nere','Scarpe Nike air zoom Uomo nere, eleganti, sportive e comode','pippo42',0,40,'30 euro','abbigliamento');")
        db.execSQL("INSERT INTO products VALUES('Panca Piana','Panca piana di dimensioni ridotte per un petto pronunciato','pippo42',2,25,'200 euro','fitness');")
        db.execSQL("INSERT INTO products VALUES('Fiat Panda','La leggenda italiana su quattro ruote a buon prezzo','utente3',3,25,'750 euro','auto');")
        db.execSQL("INSERT INTO products VALUES('Peugeout 208','La berlina francese amata da tutti, in versione grigio','utente1',3,25,'5750 euro','auto');")
        db.execSQL("INSERT INTO products VALUES('Fiat Punto','Fiat Punto Evo bianca, diesel, km zero, full optional','utente5',3,25,'7550 euro','auto');")
        db.execSQL("INSERT INTO products VALUES('Freeweight Leg Extension','Leg Extension multifunzione, adatta a qualsiasi utilizzo, comoda e motivante!','utente3',0,25,'550 euro','fitness');")
        db.execSQL("INSERT INTO products VALUES('Multipower','Attrezzo multiuso ideale per per eseguire innumerevoli esercizi assistiti.','utente2',0,25,'450 euro','fitness');")
        db.execSQL("INSERT INTO products VALUES('Dell Notebook XPS 13 9370','Notebook XPS 13 9370 con Processore di ultima generazione!.','utente2',0,25,'999 euro','tecnologia');")

        db.execSQL("DELETE FROM joiner")
        db.execSQL("INSERT INTO joiner VALUES ('Surface Pro 6','sasybello');")
        db.execSQL("INSERT INTO joiner VALUES ('Surface Pro 6','belgatto');")
        db.execSQL("INSERT INTO joiner VALUES ('Surface Pro 6','utente3');")
        db.execSQL("INSERT INTO joiner VALUES ('Surface Pro 6','utente4');")
        db.execSQL("INSERT INTO joiner VALUES ('CZZ Scrivania Pieghevole','utente4');")
        db.execSQL("INSERT INTO joiner VALUES ('CZZ Scrivania Pieghevole','utente1');")
        db.execSQL("INSERT INTO joiner VALUES ('CZZ Scrivania Pieghevole','utente3');")
        db.execSQL("INSERT INTO joiner VALUES ('CZZ Scrivania Pieghevole','sasybello');")
        db.execSQL("INSERT INTO joiner VALUES ('IphoneXS 64GB','belgatto');")
        db.execSQL("INSERT INTO joiner VALUES ('IphoneXS 64GB','utente3');")
        db.execSQL("INSERT INTO joiner VALUES ('IphoneXS 64GB','utente4');")
        db.execSQL("INSERT INTO joiner VALUES ('IphoneXS 64GB','utente1');")
        db.execSQL("INSERT INTO joiner VALUES ('Adidas Felpa Originals','belgatto');")
        db.execSQL("INSERT INTO joiner VALUES ('Adidas Felpa Originals','utente3');")
        db.execSQL("INSERT INTO joiner VALUES ('Panca Piana','utente3');")
        db.execSQL("INSERT INTO joiner VALUES ('Panca Piana','utente1');")
        db.execSQL("INSERT INTO joiner VALUES ('Fiat Panda','pippo42');")
        db.execSQL("INSERT INTO joiner VALUES ('Fiat Panda','utente1');")
        db.execSQL("INSERT INTO joiner VALUES ('Fiat Panda','utente2');")
        db.execSQL("INSERT INTO joiner VALUES ('Pegeout 208','utente4');")
        db.execSQL("INSERT INTO joiner VALUES ('Pegeout 208','utente6');")
        db.execSQL("INSERT INTO joiner VALUES ('Pegeout 208','utente5');")
        db.execSQL("INSERT INTO joiner VALUES ('Fiat Punto','belgatto');")
        db.execSQL("INSERT INTO joiner VALUES ('Fiat Punto','utente3');")
        db.execSQL("INSERT INTO joiner VALUES ('Fiat Punto','utente2');")
    }
}
