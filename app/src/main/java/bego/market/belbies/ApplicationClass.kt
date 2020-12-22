package bego.market.belbies

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.mazenrashed.printooth.Printooth


class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
         Printooth.init(this)
    }


}