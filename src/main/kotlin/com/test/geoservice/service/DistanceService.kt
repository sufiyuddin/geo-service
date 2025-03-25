package com.test.geoservice.service

import com.test.geoservice.controller.response.DistanceResponse
import com.test.geoservice.controller.response.PostcodeDto
import com.test.geoservice.repository.PostcodeLatLngRepository
import com.test.geoservice.utils.GeoUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DistanceService (
    private val repository: PostcodeLatLngRepository)
{
    private val log = LoggerFactory.getLogger(javaClass)

    fun calculateDistance(from: String, to: String): DistanceResponse {
        log.info("[DISTANCE_REQUEST] from='{}', to='{}'", from, to)

        val p1 = repository.findByPostcodeIgnoreCase(from)
            ?: throw IllegalArgumentException("From Postcode '$from' not found")

        val p2 = repository.findByPostcodeIgnoreCase(to)
            ?: throw IllegalArgumentException("To Postcode '$to' not found")

        return GeoUtils.calculateDistance(p1.latitude, p1.longitude, p2.latitude, p2.longitude)
            .also { log.info("[DISTANCE_RESPONSE] Postal codes from='{}' to='{}' is {} km",
                from, to, String.format("%.2f", it)) }
            .let { distance ->
                DistanceResponse(
                    from = PostcodeDto(p1.postcode, p1.latitude, p1.longitude),
                    to = PostcodeDto(p2.postcode, p2.latitude, p2.longitude),
                    distance = String.format("%.2f", distance).toDouble(),
                    unit = "km"
                )
            }
    }
}