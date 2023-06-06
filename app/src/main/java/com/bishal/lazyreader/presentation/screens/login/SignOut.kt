package com.bishal.lazyreader.presentation.screens.login

import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Session
import io.appwrite.services.Account

suspend fun signOut(client: Client) {

    val account = Account(client)


    try{

        val currentSession = Session
        account.deleteSession(sessionId = currentSession.toString())
    } catch (e: AppwriteException) {
        e.printStackTrace()
    }
}