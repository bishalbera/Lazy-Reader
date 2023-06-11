package com.bishal.lazyreader.domain.repository

import android.util.Log
import com.bishal.lazyreader.data.DataOrException
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.util.Constants
import com.google.gson.Gson
import io.appwrite.exceptions.AppwriteException
import io.appwrite.extensions.toJson
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AppwriteRepository @Inject constructor(private val databases: Databases ) {

    suspend fun getAllBooksFromDatabase(): Flow<DataOrException<List<MBook>, Boolean, Exception>> =
        flow {
            val dataOrException = DataOrException<List<MBook>, Boolean, Exception>(
                data = emptyList(),
                loading = true,
                e = null
            )


            try {
                dataOrException.loading = true
                val documents = databases.listDocuments(
                    databaseId = Constants.database_Id,
                    collectionId = Constants.bookCollection_Id,



                ).documents
               val books = mutableListOf<MBook>()
                val gson = Gson()
                for (document in documents) {
                   val json = document.toJson()
                    val mBook = gson.fromJson(json, MBook::class.java)
                   if (mBook == null) {
                       Log.d("Get", "Could not deserialize book from document with JSON: $json")

                   }else {
                       books.add(mBook)
                   }
                }
                Log.d("GET", "Number of books retrieved: ${books.size}")

                dataOrException.loading = false

                dataOrException.data = books

                emit(dataOrException)






            } catch (exception: AppwriteException) {
                dataOrException.e = exception
                dataOrException.data = emptyList()
                emit(dataOrException)
            }
        }
}