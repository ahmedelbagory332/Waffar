package bego.market.waffar

import android.app.Application
import com.mazenrashed.printooth.Printooth
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
         Printooth.init(this)
    }


}