package com.test.geoservice.service

import com.test.geoservice.controller.request.PostcodeRequest
import org.springframework.stereotype.Service
import com.test.geoservice.controller.response.PostcodeDto
import com.test.geoservice.repository.PostcodeLatLngRepository

@Service
class PostCodeLatLngService (
    private val repository: PostcodeLatLngRepository
) {
    fun updatePostcode(postcode: String, request: PostcodeRequest): PostcodeDto {
        return repository.findByPostcodeIgnoreCase(postcode)
            ?.apply {
                latitude = request.latitude!!
                longitude = request.longitude!!
            }
            ?.let { updated ->
                repository.save(updated)
            }
            ?.let { saved ->
                PostcodeDto(saved.postcode, saved.latitude, saved.longitude)
            }
            ?: throw IllegalArgumentException("Postcode '$postcode' not found")
    }
}