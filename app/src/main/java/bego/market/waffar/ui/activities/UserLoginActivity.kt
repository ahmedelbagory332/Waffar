package bego.market.waffar.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import bego.market.waffar.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLoginActivity : AppCompatActivity() {

    private lateinit var naveController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        naveController = this.findNavController(R.id.myNavHostLoginFragment)

    }


}