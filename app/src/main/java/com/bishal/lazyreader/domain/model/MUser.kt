package com.bishal.lazyreader.domain.model

data class MUser(val id: String?,
                 val userId: String,
                 val displayName: String,
                 val quote: String,
                 val profession: String){
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf("user-id" to this.userId,
            "display-Name" to this.displayName,
            "quote" to this.quote,
            "profession" to this.profession,
           )
    }

}


