package io.limberapp.web.api

import io.limberapp.common.serialization.Json
import io.limberapp.backend.module.forms.rep.formsSerializersModule

internal val json = Json(serializersModule = formsSerializersModule)
