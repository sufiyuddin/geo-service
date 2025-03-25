package com.test.geoservice.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class PostcodeRequest(
    @Schema(description = "Latitude in degrees", example = "52.123456")
    @NotNull
    val latitude: Double? = null,

    @Schema(description = "Longitude in degrees", example = "-1.234567")
    @NotNull
    val longitude: Double? = null
)