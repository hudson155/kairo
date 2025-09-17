package kairo.util

import com.google.common.io.Resources
import java.net.URL

public fun resource(resourceName: String): URL =
  Resources.getResource(resourceName)
