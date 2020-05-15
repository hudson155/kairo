package io.limberapp.web.context

import react.*

internal data class ProviderValue<Value : Any>(val value: Value) : RProps
