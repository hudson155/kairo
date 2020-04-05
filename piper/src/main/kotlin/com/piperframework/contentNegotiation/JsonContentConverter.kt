package com.piperframework.contentNegotiation

import com.piperframework.serialization.Json
import io.ktor.features.ContentConverter
import io.ktor.jackson.JacksonConverter

class JsonContentConverter(json: Json) : ContentConverter by JacksonConverter(json.objectMapper)
