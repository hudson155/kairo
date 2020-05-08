package io.limberapp.web.util

import org.w3c.dom.events.Event

internal val Event.keyCode: Int get() = asDynamic().keyCode as Int

internal val Event.targetValue: String get() = checkNotNull(target).asDynamic().value as String
