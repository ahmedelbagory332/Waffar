package bego.market.belbies.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import bego.market.belbies.R
import bego.market.belbies.UserValidation


class MainActivity : AppCompatActivity() {
    lateinit var userValidation: UserValidation




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userValidation = UserValidation(this)

         if(userValidation.readLoginStatusAdmin()){

             Thread(Runnable {
                 try {
                     Thread.sleep(3000)
                     startActivity(Intent(applicationContext, AdminActivity::class.java))
                     finish()
                 } catch (e: InterruptedException) {
                     e.printStackTrace()
                 }
             }).start()
        }
        else
        {
            Thread(Runnable {
                try {
                    Thread.sleep(3000)
                    startActivity(Intent(applicationContext, HomePage::class.java))
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }).start()

        }



    }


}