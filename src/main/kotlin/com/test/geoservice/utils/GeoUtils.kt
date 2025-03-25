package com.test.geoservice.utils

import kotlin.math.*

object GeoUtils {
    private const val EARTH_RADIUS_KM = 6371.0

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val lon1Rad = Math.toRadians(lon1)
        val lon2Rad = Math.toRadians(lon2)

        val a = haversine(lat1Rad, lat2Rad) +
                cos(lat1Rad) * cos(lat2Rad) * haversine(lon1Rad, lon2Rad)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_KM * c
    }

    private fun haversine(deg1: Double, deg2: Double): Double {
        return (sin((deg1 - deg2) / 2.0)).pow(2)
    }
}