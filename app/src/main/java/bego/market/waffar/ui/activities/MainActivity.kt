package bego.market.waffar.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bego.market.waffar.R
import bego.market.waffar.utils.UserValidation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userValidation: UserValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


         if(userValidation.readLoginStatusAdmin()){

             Thread {
                 try {
                     Thread.sleep(3000)
                     startActivity(Intent(applicationContext, AdminActivity::class.java))
                     finish()
                 } catch (e: InterruptedException) {
                     e.printStackTrace()
                 }
             }.start()
        }
        else
        {
            Thread {
                try {
                    Thread.sleep(3000)
                    startActivity(Intent(applicationContext, HomePage::class.java))
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.start()

        }



    }


}