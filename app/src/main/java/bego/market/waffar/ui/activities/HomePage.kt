package bego.market.waffar.ui.activities


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import bego.market.waffar.R
import bego.market.waffar.utils.UserValidation
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomePage : AppCompatActivity() {


    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var drawerLayout: DrawerLayout
    lateinit var naveController: NavController
    @Inject
    lateinit var userValidation: UserValidation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        naveController = this.findNavController(R.id.myNavHostFragment)
        val naveView: NavigationView = findViewById(R.id.navView)
        val navMenu: Menu = naveView.menu
        drawerLayout = findViewById(R.id.drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, naveController, drawerLayout)//show drawer icon
        appBarConfiguration = AppBarConfiguration(naveController.graph, drawerLayout)
        NavigationUI.setupWithNavController(naveView, naveController)// item in list action


        if (userValidation.readLoginStatus()) {

            navMenu.findItem(R.id.loginActivity).isVisible = false
            navMenu.findItem(R.id.logoutActivity).isVisible = true
            navMenu.findItem(R.id.reportFragment).isVisible = true
            navMenu.findItem(R.id.myAddress).isVisible = true

        }
        else {
            navMenu.findItem(R.id.logoutActivity).isVisible = false
            navMenu.findItem(R.id.loginActivity).isVisible = true
            navMenu.findItem(R.id.reportFragment).isVisible = false
            navMenu.findItem(R.id.myAddress).isVisible = false


        }





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



        override fun onSupportNavigateUp() : Boolean{
            return   NavigationUI.navigateUp(naveController, appBarConfiguration) || super.onSupportNavigateUp()
        }


    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

     fun closeDrawer(item: MenuItem) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

}