package cz.hlinkapp.gvpintranet.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.hlinkapp.gvpintranet.view_models.ArticleDetailViewModel
import cz.hlinkapp.gvpintranet.view_models.ContentAddViewModel
import cz.hlinkapp.gvpintranet.view_models.MainViewModel
import cz.hlinkapp.gvpintranet.view_models.config.MyViewModelFactory
import cz.hlinkapp.gvpintranet.view_models.config.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * A Dagger2 module which provides ViewModel-related classes.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel (mainViewModel: MainViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleDetailViewModel::class)
    abstract fun bindArticleDetailViewModel (articleDetailViewModel: ArticleDetailViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContentAddViewModel::class)
    abstract fun bindContentAddViewModel (contentAddViewModel: ContentAddViewModel) : ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: MyViewModelFactory): ViewModelProvider.Factory
}