package cz.hlinkapp.gvpintranet.di.components

import android.app.Application
import cz.hlinkapp.gvpintranet.activities.MainActivity
import cz.hlinkapp.gvpintranet.di.MyApplication
import cz.hlinkapp.gvpintranet.di.modules.*
import cz.hlinkapp.gvpintranet.fragments.content_add_fragments.AddArticleFragment
import cz.hlinkapp.gvpintranet.fragments.content_add_fragments.AddEventFragment
import cz.hlinkapp.gvpintranet.fragments.detail.ArticleDetailFragment
import cz.hlinkapp.gvpintranet.fragments.main_fragments.ArticlesFragment
import cz.hlinkapp.gvpintranet.fragments.main_fragments.NewsFragment
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Main app component for Dagger2.
 */
@Singleton
@Component(modules = [
    ApplicationModule::class,
    AndroidInjectionModule::class,
    RoomModule::class,
    ViewModelModule::class,
    UtilsModule::class,
    RetrofitModule::class])
interface MyAppComponent {
    fun inject(application: MyApplication)

    fun inject(activity: MainActivity)

    fun inject(fragment: ArticlesFragment)
    fun inject (fragment: NewsFragment)
    fun inject (fragment: ArticleDetailFragment)
    fun inject (fragment: AddArticleFragment)
    fun inject (fragment: AddEventFragment)

    fun application() : Application
}