package com.bishal.lazyreader.domain.repository

import com.bishal.lazyreader.data.DataOrException
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.util.Constants
import io.appwrite.exceptions.AppwriteException
import io.appwrite.extensions.jsonCast
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AppwriteRepository @Inject constructor(private val databases: Databases) {

    suspend fun getAllBooksFromDatabase(): Flow<DataOrException<List<MBook>, Boolean, Exception>> =
        flow {
            val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()


            try {
                dataOrException.loading = true
                val books = databases.listDocuments(
                    databaseId = Constants.database_Id,
                    collectionId = Constants.bookCollection_Id,



                )

                dataOrException.data = books.documents.map{ document ->
                    document.jsonCast(MBook::class.java)

                }
                emit(dataOrException)






            } catch (exception: AppwriteException) {
                dataOrException.e = exception
                emit(dataOrException)
            }
        }
}