package io.limberapp.web.api

import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formsSerialModule

internal val json = Json(context = formsSerialModule)
