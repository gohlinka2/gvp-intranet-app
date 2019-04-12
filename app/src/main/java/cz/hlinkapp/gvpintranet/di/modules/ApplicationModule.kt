package cz.hlinkapp.gvpintranet.di.modules

import android.app.Application
import cz.hlinkapp.gvpintranet.di.MyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * A Dagger2 module which provides the Application class.
 */
@Module
class ApplicationModule (private val application: MyApplication){

    @Singleton
    @Provides
    fun provideApplication(): Application = application
}