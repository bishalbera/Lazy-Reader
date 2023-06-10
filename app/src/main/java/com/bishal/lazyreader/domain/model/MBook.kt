package com.bishal.lazyreader.domain.model

import java.sql.Timestamp

data class MBook(
    var id: String? = null,
    var title: String? = null,
    var authors: String? = null,
    var notes: String? = null,
    var photoUrl: String? = null,
    var categories: String? = null,
    var publishedDate: String? = null,
    var rating: Int? = null,
    var description: String? = null,
    var pageCount: String? = null,
    var startedReading: Timestamp? = null,
    var finishedReading: Timestamp? = null,
    var userId: String? = null,
    var googleBookId: String? = null
) {
    fun toHashMap(): Any {
        return hashMapOf(
            "title" to this.title,
            "authors" to this.authors,
            "notes" to this.notes,
            "photo-Url" to this.photoUrl,
            "categories" to this.categories,
            "published-Date" to this.publishedDate, //convert date to Long (timestamp)
            "rating" to this.rating, // cast Double? to Double, with default of 0.0
            "description" to this.description,
            "page-Count" to this.pageCount, // cast Int? to Int, with default of 0
            "started-Reading" to this.startedReading?.time, //convert date to Long (timestamp)
            "finished-Reading" to this.finishedReading?.time, //convert date to Long (timestamp)
            "user-Id" to this.userId,
            "google-Book-Id" to this.googleBookId
        )
    }
}
