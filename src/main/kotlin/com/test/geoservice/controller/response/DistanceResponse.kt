package com.test.geoservice.controller.response

data class DistanceResponse(
    val from: PostcodeDto,
    val to: PostcodeDto,
    val distance: Double,
    val unit: String = "km"
)