package com.test.geoservice.repository

import com.test.geoservice.entity.PostcodeLatLng
import org.springframework.data.jpa.repository.JpaRepository

interface PostcodeLatLngRepository : JpaRepository<PostcodeLatLng, Long> {
    fun findByPostcodeIgnoreCase(postcode: String): PostcodeLatLng?
}