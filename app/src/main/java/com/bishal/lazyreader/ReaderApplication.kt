package com.bishal.lazyreader

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class ReaderApplication : Application() {

    lateinit var client: Client
    lateinit var account: Account

    override fun onCreate() {
        super.onCreate()

        // Delayed initialization
        client = Client(context = applicationContext)
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject("64636f4a047d4c71961a")
            .setSelfSigned(true)
        account = Account(client)

        // Call the function to create the user
        createUser()
    }

    private fun createUser() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val user = account.create(
                    userId = ID.unique(),
                    email = "biplab@bishal.com",
                    password = "iambishal"
                )
                // Handle the user creation response
                Log.d("Appwrite user", user.toMap()?.toString()!!)
            } catch (e: AppwriteException) {
                // Handle any errors that occur during user creation

                e.printStackTrace()
            }
        }
    }
}
