package com.test.geoservice.controller

import com.test.geoservice.controller.request.PostcodeRequest
import com.test.geoservice.service.DistanceService
import com.test.geoservice.service.PostCodeLatLngService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GeoController(
    private val distanceService: DistanceService,
    private val postCodeLatLngService: PostCodeLatLngService
) : GeoApi {

    override fun getDistance(
        postCodeFrom: String,
        postCodeTo: String
    ) = distanceService.calculateDistance(postCodeFrom, postCodeTo).let { ResponseEntity.ok(it) }

    override fun updatePostcode(
        postcode: String,
        request: PostcodeRequest,
    ) = postCodeLatLngService.updatePostcode(postcode, request).let { ResponseEntity.ok(it) }
}