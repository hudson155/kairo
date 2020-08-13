package io.limberapp.web.api

import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formsSerializersModule

internal val json = Json(serializersModule = formsSerializersModule)
