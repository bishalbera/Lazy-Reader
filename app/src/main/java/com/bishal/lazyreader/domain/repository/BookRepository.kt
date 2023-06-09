package com.bishal.lazyreader.domain.repository

import com.bishal.lazyreader.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {

}