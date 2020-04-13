package io.limberapp.web.context

import react.RProps

internal data class ProviderValue<Value : Any>(val value: Value) : RProps
