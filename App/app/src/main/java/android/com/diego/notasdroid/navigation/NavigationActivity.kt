package android.com.diego.notasdroid.navigation

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.datos.UserSQLite
import android.com.diego.notasdroid.utilidades.Utilidades
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class NavigationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration


    companion object{
        lateinit var user : UserSQLite
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        //Creamos un nuevo nav Header y buscamos sus elementos por el ID
        val navHeader = navView.getHeaderView(0)
        val imgUser = navHeader.findViewById<ImageView>(R.id.imgUserPhoto_slideshow)
        val nameUser = navHeader.findViewById<TextView>(R.id.textNameUser_slideshow)
        val emailUser = navHeader.findViewById<TextView>(R.id.textEmailUser_slideshow)

        //Buscamos al usuario y asignamos sus datos a los elementos del nav header
        val email = intent.getStringExtra("EMAIL")
        user = SQLiteControlador.selectUsuario(email, this)!!
        imgUser.setImageBitmap(Utilidades.base64ToBitmap(user!!.img))
        nameUser.text = user!!.name
        emailUser.text = user!!.email


        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}