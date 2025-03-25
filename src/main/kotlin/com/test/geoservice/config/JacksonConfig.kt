package com.test.geoservice.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    /**
     * Constructs and configures an [ObjectMapper].
     *
     * The [ObjectMapper] will have the following configuration applied:
     * - any Jackson modules available on the class path are discovered and registered
     * - date values will **not** be serialized as timestamps but dates only (without time)
     * - unknown properties discovered on deserialization will raise an exception
     * - properties with `null` values will **not** be serialized
     */
    @Bean
    fun getObjectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .findAndRegisterModules()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
}