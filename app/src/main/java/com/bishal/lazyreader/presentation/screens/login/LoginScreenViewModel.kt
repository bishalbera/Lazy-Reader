package com.bishal.lazyreader.presentation.screens.login



import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bishal.lazyreader.domain.model.MUser
import com.bishal.lazyreader.util.Constants
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException


class LoginScreenViewModel( application: Application) : AndroidViewModel(application) {


    private val client = Client(context = application)
        .setEndpoint(Constants.appwrite_Endpoint)
        .setProject(Constants.project_Id)

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                val account = Account(client)
                val user = account.createEmailSession(
                    email = email,
                    password = password,
                )

                Log.d(
                    "Appwrite",
                    "signInWithEmailAndPassword: Yayayay!"
                )
                //TODO:take them home screen
                home()

            } catch (ex: AppwriteException) {
                Log.d("Appwrite", "signinwithemailandpassword: ${ex.message}")
            }

        }

     fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) = viewModelScope.launch {
        if (_loading.value == false) {
            _loading.value = true
            val account = Account(client)
            val user = account.create(
                userId = ID.unique(),
                email = email,
                password = password,
            )

            val displayName = user.email.split('@')[0]
            createUser(displayName)
            home()
            _loading.value = false
        }


    }

    private suspend fun createUser(displayName: String?) {
        val user = MUser(
            userId = UUID.randomUUID().toString(),
            displayName = displayName.toString(),
            quote = "Enjoy every instant of your life",
            profession = "Android Developer",
            id = null
        ).toMap()

        val databases = Databases(client)

        try {
            databases.createDocument(
                databaseId = Constants.database_Id,
                collectionId = Constants.collection_Id,
                documentId = ID.unique(),
                data = user
            )
            Log.d("Appwrite", "User created successfully")
        } catch (e: CancellationException) {
            Log.e("Appwrite", "Coroutine cancelled", e)
        } catch (e: Exception) {
            Log.e("Appwrite", "Error: " + e.message, e)
        }
    }
}

