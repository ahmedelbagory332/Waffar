package bego.market.belbies.Activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MessengerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            val uri: Uri = Uri.parse("https://www.messenger.com/t/belbiesmarket")
            val i = Intent(Intent.ACTION_VIEW, uri)
            startActivity(i)
            finish()


    }
}