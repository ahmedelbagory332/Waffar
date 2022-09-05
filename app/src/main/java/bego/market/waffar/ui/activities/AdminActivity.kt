package bego.market.waffar.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import bego.market.waffar.R
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var drawerLayout: DrawerLayout
    private lateinit var naveController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        naveController = this.findNavController(R.id.myNavHostFragmentAdmin)
        val naveView: NavigationView = findViewById(R.id.navViewAdmin)
        drawerLayout = findViewById(R.id.drawerLayoutAdmin)
        NavigationUI.setupActionBarWithNavController(this, naveController, drawerLayout)//show drawer icon
        appBarConfiguration = AppBarConfiguration(naveController.graph, drawerLayout)
        NavigationUI.setupWithNavController(naveView, naveController)// item in list action

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }else{
                    NavigationUI.navigateUp(naveController, appBarConfiguration)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

    }
    override fun onSupportNavigateUp(): Boolean {
        return   NavigationUI.navigateUp(naveController, appBarConfiguration) || super.onSupportNavigateUp()
    }
    fun closeDrawer(item: MenuItem) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

}