package bego.market.belbies.Activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class RateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val uri: Uri = Uri.parse("market://details?id=$packageName")
            val i = Intent(Intent.ACTION_VIEW, uri)
            startActivity(i)
            finish()
        } catch (e: ActivityNotFoundException) {
            val uri: Uri =
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
            val i = Intent(Intent.ACTION_VIEW, uri)
            startActivity(i)
            finish()
        }

    }
}