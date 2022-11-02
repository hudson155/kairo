package limber.config.rest

public data class RestClientConfig<out T : Any>(val baseUrls: T)
