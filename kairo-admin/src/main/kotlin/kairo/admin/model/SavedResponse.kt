package kairo.admin.model

public data class SavedResponse(
  val status: Int,
  val statusText: String,
  val elapsedMs: Int,
  val body: String,
) {
  val isOk: Boolean get() = status in 200..299

  public companion object {
    @Suppress("SwallowedException")
    public fun fromJson(json: String, body: String): SavedResponse? {
      // Parses {"s":200,"t":"OK","ms":42}
      val sMatch = Regex(""""s"\s*:\s*(\d+)""").find(json) ?: return null
      val tMatch = Regex(""""t"\s*:\s*"([^"]*?)"""").find(json)
      val msMatch = Regex(""""ms"\s*:\s*(\d+)""").find(json)
      return SavedResponse(
        status = sMatch.groupValues[1].toInt(),
        statusText = tMatch?.groupValues?.get(1).orEmpty(),
        elapsedMs = msMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0,
        body = body,
      )
    }
  }
}
