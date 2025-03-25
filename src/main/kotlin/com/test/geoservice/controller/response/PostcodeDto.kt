package com.test.geoservice.controller.response

data class PostcodeDto(
    val postcode: String,
    val latitude: Double,
    val longitude: Double
)