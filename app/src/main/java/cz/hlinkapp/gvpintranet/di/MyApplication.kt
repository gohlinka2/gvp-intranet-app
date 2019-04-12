package cz.hlinkapp.gvpintranet.di

import android.app.Application
import cz.hlinkapp.gvpintranet.di.components.DaggerMyAppComponent
import cz.hlinkapp.gvpintranet.di.components.MyAppComponent
import cz.hlinkapp.gvpintranet.di.modules.ApplicationModule
import cz.hlinkapp.gvpintranet.di.modules.RetrofitModule
import cz.hlinkapp.gvpintranet.di.modules.RoomModule
import cz.hlinkapp.gvpintranet.di.modules.UtilsModule

/**
 * The app's Application class.
 */
class MyApplication : Application(){

    private lateinit var myAppComponent: MyAppComponent

    override fun onCreate() {
        super.onCreate()

        myAppComponent = DaggerMyAppComponent
            .builder()
            .roomModule(RoomModule(this))
            .applicationModule(ApplicationModule(this))
            .utilsModule(UtilsModule(this))
            .retrofitModule(RetrofitModule())
            .build()
    }

    /**
     * Returns the Dagger2 main application component.
     */
    fun getApplicationComponent(): MyAppComponent = myAppComponent
}