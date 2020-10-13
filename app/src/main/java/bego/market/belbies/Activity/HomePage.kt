package bego.market.belbies.Activity


 import android.Manifest
 import android.content.pm.PackageManager
 import android.os.Bundle
 import android.view.Menu
 import android.widget.TextView
 import android.widget.Toast
 import androidx.appcompat.app.AppCompatActivity
 import androidx.core.app.ActivityCompat
 import androidx.core.content.ContextCompat
 import androidx.core.view.GravityCompat
 import androidx.drawerlayout.widget.DrawerLayout
 import androidx.navigation.NavController
 import androidx.navigation.findNavController
 import androidx.navigation.ui.AppBarConfiguration
 import androidx.navigation.ui.NavigationUI
 import bego.market.belbies.R
 import bego.market.belbies.UserValidation
 import com.google.android.material.navigation.NavigationView


class HomePage : AppCompatActivity() {

    private val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 100
    private val INTERNET_PERMISSION_CODE = 101
    private val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 102
    private val ACCESS_NETWORK_STATE_PERMISSION_CODE = 103

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var drawerLayout: DrawerLayout
    lateinit var naveController: NavController
    lateinit var userValidation: UserValidation
    var textCartItemCount: TextView? = null
    var mCartItemCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        naveController = this.findNavController(R.id.myNavHostFragment)
        val naveView: NavigationView = findViewById(R.id.navView)
        drawerLayout = findViewById(R.id.drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, naveController, drawerLayout)//show drawer icon
        appBarConfiguration = AppBarConfiguration(naveController.graph, drawerLayout)
        NavigationUI.setupWithNavController(naveView, naveController)// item in list action

        userValidation = UserValidation(this)

        val navMenu: Menu = naveView.menu
        if (userValidation.readLoginStatus()){

            navMenu.findItem(R.id.loginActivity).isVisible = false
            navMenu.findItem(R.id.logoutActivity).isVisible = true
            navMenu.findItem(R.id.reportFragment).isVisible = true
            navMenu.findItem(R.id.myAddress).isVisible = true

        }
        else{
            navMenu.findItem(R.id.logoutActivity).isVisible = false
            navMenu.findItem(R.id.loginActivity).isVisible = true
            navMenu.findItem(R.id.reportFragment).isVisible = false
            navMenu.findItem(R.id.myAddress).isVisible = false



        }
        checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
        checkPermissions(Manifest.permission.INTERNET,INTERNET_PERMISSION_CODE)
        checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE_PERMISSION_CODE)
        checkPermissions(Manifest.permission.ACCESS_NETWORK_STATE,ACCESS_NETWORK_STATE_PERMISSION_CODE)

    }
    // this method for drawer icon action
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

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onStop() {
        super.onStop()
         finish()
    }

    fun checkPermissions(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            //Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == INTERNET_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == ACCESS_NETWORK_STATE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}