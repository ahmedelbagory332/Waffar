package bego.market.waffar.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bego.market.waffar.R
import bego.market.waffar.utils.UserValidation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogoutActivity : AppCompatActivity() {

    @Inject
    lateinit var userValidation: UserValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userValidation.writeLoginStatus(false)
        userValidation.writeEmail("")
        userValidation.writeLoginStatusAdmin(false)
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()


    }
}