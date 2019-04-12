package cz.hlinkapp.gvpintranet.di.modules

import cz.hlinkapp.gvpintranet.contracts.ServerContract.Companion.MAIN_API_URL
import cz.hlinkapp.gvpintranet.data.data_sources.server.IntranetService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * A Dagger2 module which provides Retrofit-related classes.
 */
@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl(MAIN_API_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideIntranetService(retrofit: Retrofit) : IntranetService {
        return retrofit.create(IntranetService::class.java)
    }

}