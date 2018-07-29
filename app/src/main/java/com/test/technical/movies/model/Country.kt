package com.test.technical.movies.model

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("iso_3166_1") val iso: String,
    val name: String
)
