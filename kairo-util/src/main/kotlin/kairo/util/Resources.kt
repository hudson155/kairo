package kairo.util

import com.google.common.io.Resources
import java.net.URL

/**
 * Syntactic sugar for [Resources.getResource].
 */
public fun resource(resourceName: String): URL =
  Resources.getResource(resourceName)
