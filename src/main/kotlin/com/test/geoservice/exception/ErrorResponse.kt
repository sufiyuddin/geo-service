package com.test.geoservice.exception

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ErrorResponse(
    @Schema(description = "Date time", example = "2025-03-24T15:33:18.361")
    val timestamp: LocalDateTime,
    @Schema(description = "Http status code", example = "400")
    val status: Int,
    @Schema(description = "Http status phrase", example = "Bad Request")
    val error: String,
    @Schema(description = "Error reason", example = "Invalid request")
    val message: String,
    @Schema(description = "Path", example = "/api/distance")
    val path: String
)