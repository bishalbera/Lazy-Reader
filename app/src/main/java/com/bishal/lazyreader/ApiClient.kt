package com.bishal.lazyreader

import android.content.Context
import com.bishal.lazyreader.util.Constants
import io.appwrite.Client

object ApiClient {
    fun createClient(context: Context): Client {

        val client = Client(context = context)
            .setEndpoint(Constants.appwrite_Endpoint)
            .setProject(Constants.project_Id)

        return client
    }
}