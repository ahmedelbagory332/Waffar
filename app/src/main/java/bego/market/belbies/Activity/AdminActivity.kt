package bego.market.belbies.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import bego.market.belbies.R
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var drawerLayout: DrawerLayout
    lateinit var naveController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        naveController = this.findNavController(R.id.myNavHostFragmentAdmin)
        val naveView: NavigationView = findViewById(R.id.navViewAdmin)
        drawerLayout = findViewById(R.id.drawerLayoutAdmin)
        NavigationUI.setupActionBarWithNavController(this, naveController, drawerLayout)//show drawer icon
        appBarConfiguration = AppBarConfiguration(naveController.graph, drawerLayout)
        NavigationUI.setupWithNavController(naveView, naveController)// item in list action
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(naveController, appBarConfiguration)
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}