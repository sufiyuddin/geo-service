package com.test.geoservice.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import com.test.geoservice.exception.ErrorResponse

@Component
class CustomAuthEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.warn("Unauthorized access attempt: ${authException.message}")

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpServletResponse.SC_UNAUTHORIZED,
            error = "Unauthorized",
            message = "Invalid credentials",
            path = request.requestURI
        )

        response.contentType = "application/json"
        response.status = errorResponse.status
        response.writer.apply {
            write(objectMapper.writeValueAsString(errorResponse))
            flush()
            close()
        }
    }
}