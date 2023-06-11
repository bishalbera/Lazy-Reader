package com.bishal.lazyreader.di

import android.app.Application
import android.content.Context
import com.bishal.lazyreader.ApiClient
import com.bishal.lazyreader.domain.repository.AppwriteRepository
import com.bishal.lazyreader.network.BooksApi
import com.bishal.lazyreader.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Databases
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppwriteBookRepository(context: Context): AppwriteRepository {
        val client: Client = ApiClient.createClient(context = context)
        return AppwriteRepository(

            databases = Databases(client)
        )
    }

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }


    @Singleton
    @Provides
    fun provideBookApi(): BooksApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

}
