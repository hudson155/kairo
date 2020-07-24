package io.limberapp.web.state

import react.*

internal data class ProviderValue<Value : Any>(val value: Value) : RProps
