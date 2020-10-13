package bego.market.belbies.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bego.market.belbies.R
import bego.market.belbies.UserValidation


class LogoutActivity : AppCompatActivity() {
    lateinit var userValidation: UserValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userValidation = UserValidation(this)
        userValidation.writeLoginStatus(false)
        userValidation.writeEmail("")
        userValidation.writeLoginStatusAdmin(false)
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        );
        finish()


    }
}