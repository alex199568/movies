package com.test.technical.movies.model

import com.google.gson.annotations.SerializedName

data class Language(
    @SerializedName("iso_639_1") val iso: String,
    val name: String
)
