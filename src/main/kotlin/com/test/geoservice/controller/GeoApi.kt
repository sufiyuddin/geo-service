package com.test.geoservice.controller

import com.test.geoservice.controller.request.PostcodeRequest
import com.test.geoservice.controller.response.DistanceResponse
import com.test.geoservice.controller.response.PostcodeDto
import com.test.geoservice.exception.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Tag(
    name = "Geographic Api",
    description = "Geographic operations"
)
@RequestMapping("/api")
@Validated
interface GeoApi {

    @Operation(
        summary = "Distance information",
        description = "Retrieve geographic (straight line) distance between two postal codes",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Distance successfully retrieved"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid arguments specified. See response message",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "5XX",
                description = "Unknown server error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @ResponseBody
    @GetMapping("/distance", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getDistance(
        @RequestParam(value = "from") postCodeFrom: String,
        @RequestParam(value = "to") postCodeTo: String
    ): ResponseEntity<DistanceResponse>


    @Operation(
        summary = "Update postcode coordinates",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Coordinate updated successfully"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid arguments specified. See response message",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Postal codes could not be found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "5XX",
                description = "Unknown server error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PutMapping("/coordinates/{postcode}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updatePostcode(
        @PathVariable(value = "postcode") postcode: String,
        @RequestBody @Valid request: PostcodeRequest
    ): ResponseEntity<PostcodeDto>
}