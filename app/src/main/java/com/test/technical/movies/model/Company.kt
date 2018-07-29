package com.test.technical.movies.model

import com.google.gson.annotations.SerializedName

data class Company(
    val id: Int,
    @SerializedName("logo_path") val logoPath: String,
    val name: String,
    @SerializedName("origin_country") val originCountry: String
)
