package com.test.geoservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.test.geoservice.GeoServiceApplication
import com.test.geoservice.controller.request.PostcodeRequest
import com.test.geoservice.controller.response.DistanceResponse
import com.test.geoservice.controller.response.PostcodeDto
import com.test.geoservice.service.DistanceService
import com.test.geoservice.service.PostCodeLatLngService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.net.URLEncoder

@SpringBootTest(
    classes = [GeoServiceApplication::class]
)
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class GeoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var distanceService: DistanceService

    @MockkBean
    private lateinit var postCodeLatLngService: PostCodeLatLngService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @WithMockUser(username = "admin", roles = ["USER"])
    fun `should retrieve the distance with correct distance info sucessfully`() {
        val from = "AB10 1XG"
        val to = "AB11 5QN"
        val response = DistanceResponse(
            from = PostcodeDto(from, 57.144165, -2.114848),
            to = PostcodeDto(to, 57.142701, -2.093295),
            distance = 1.31,
            unit = "km"
        )

        every { distanceService.calculateDistance(any(), any()) } returns response

        val encodedFrom = URLEncoder.encode(from, "UTF-8")
        val encodedTo = URLEncoder.encode(to, "UTF-8")

        mockMvc.perform(
            get("/api/distance?from=$encodedFrom&to=$encodedTo")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk)
        .andExpect(jsonPath("$.from.postcode").value(from))
        .andExpect(jsonPath("$.to.postcode").value(to))
        .andExpect(jsonPath("$.distance").value(1.31))
        .andExpect(jsonPath("$.unit").value("km"))
    }

    @Test
    fun `should return unauthorized when credential is wrong or not provided`() {
        val from = URLEncoder.encode("AB10 1XG", "UTF-8")
        val to = URLEncoder.encode("AB11 5QN", "UTF-8")

        mockMvc.perform(
            get("/api/distance?from=$from&to=$to")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized)
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andExpect(jsonPath("$.message").value("Invalid credentials"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["USER"])
    fun `should return 400 when required parameter is missing`() {
        val from = URLEncoder.encode("AB10 1XG", "UTF-8")

        mockMvc.perform(
            get("/api/distance?from=$from")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest)
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Missing required parameter: to"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["USER"])
    fun `should return 404 when postcode not found`() {
        val from = "AB10 1XG"
        val to = "AB11 51" // Non-existent

        every { distanceService.calculateDistance(any(), any()) } throws
                IllegalArgumentException("To Postcode '$to' not found")

        val encodedFrom = URLEncoder.encode(from, "UTF-8")
        val encodedTo = URLEncoder.encode(to, "UTF-8")

        mockMvc.perform(
            get("/api/distance?from=$encodedFrom&to=$encodedTo")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound)
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("To Postcode '$to' not found"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["USER"])
    fun `should return 500 when unknown error occurs`() {
        val from = "AB10 1XG"
        val to = "AB11 5QN"

        every { distanceService.calculateDistance(from, to) } throws RuntimeException("Exception")

        val encodedFrom = URLEncoder.encode(from, "UTF-8")
        val encodedTo = URLEncoder.encode(to, "UTF-8")

        mockMvc.perform(
            get("/api/distances?from=$encodedFrom&to=$encodedTo")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isInternalServerError)
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["USER"])
    fun `should update postcode successfully`() {

        // ---- SET UP ----------------------------------------------------------------------------
        val postcode = "AB10 1XG"
        val lat = 2.0
        val lon = 1.0
        val request = PostcodeRequest(lat, lon)
        val response = PostcodeDto(postcode, lat, lon)

        every { postCodeLatLngService.updatePostcode(any(), any()) } returns response
        val encodedPostcode = URLEncoder.encode(postcode, "UTF-8")

        // ---- EXECUTE & VERIFY-------------------------------------------------------------------
        mockMvc.perform(
            put("/api/coordinates/$encodedPostcode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk)
        .andExpect(jsonPath("$.postcode").value(postcode))
        .andExpect(jsonPath("$.latitude").value(lat))
        .andExpect(jsonPath("$.longitude").value(lon))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["USER"])
    fun `should return 400 when payload is invalid JSON`() {
        val malformedJson = "{'latitude': 2.0, 'longitude': 1.0}"
        val postcode = "AB10 1XG"
        val encodedPostcode = URLEncoder.encode(postcode, "UTF-8")

        mockMvc.perform(
            put("/api/coordinates/$encodedPostcode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson)
        )
        .andExpect(status().isBadRequest)
        .andExpect(jsonPath("$.status").value(400))
    }
}