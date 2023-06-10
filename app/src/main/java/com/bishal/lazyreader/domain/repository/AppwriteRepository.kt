package com.bishal.lazyreader.domain.repository

import com.bishal.lazyreader.domain.model.MBook
import io.appwrite.services.Databases
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AppwriteRepository @Inject constructor(private val databases: Databases) {

    suspend fun getAllBooksFromDatabase(userId: String): Flow<DataOrException<List<MBook>, Boolean, Exception>> =
        flow {
            val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

            try {
                dataOrException.loading = true
                val books = database.listDocuments("books", ArrayList<String>().apply { add("userId=$userId") })
                dataOrException.data = books.documents.map { document ->
                    document.toObject(MBook::class.java)!!
                }
                emit(dataOrException)

                val subscription = database.subscribe(listOf("books"), ArrayList<String>().apply { add("userId=$userId") })
                val listener = object : DocumentListener {
                    override fun onDocumentAdd(document: Document) {
                        updateData()
                    }

                    override fun onDocumentUpdate(document: Document) {
                        updateData()
                    }

                    override fun onDocumentDelete(document: Document) {
                        updateData()
                    }

                    private fun updateData() {
                        dataOrException.loading = false
                        dataOrException.data = subscription.documents.map { document ->
                            document.toObject(MBook::class.java)!!
                        }
                        emit(dataOrException)
                    }
                }
                subscription.addDocumentListener(listener)
                emit(dataOrException)

            } catch (exception: AppwriteException) {
                dataOrException.e = exception
                emit(dataOrException)
            }
        }
}