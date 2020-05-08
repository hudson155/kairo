@file:Suppress("TopLevelPropertyNaming")

package io.limberapp.web.util

import kotlin.browser.window

internal val rootDomain = window.location.host
internal val rootUrl = "${window.location.protocol}//$rootDomain"
internal const val rootPath = "/"
