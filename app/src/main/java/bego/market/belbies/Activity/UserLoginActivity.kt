package bego.market.belbies.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import bego.market.belbies.R

class UserLoginActivity : AppCompatActivity() {

    lateinit var naveController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        naveController = this.findNavController(R.id.myNavHostLoginFragment)

    }


}