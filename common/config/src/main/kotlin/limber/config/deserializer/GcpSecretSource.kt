package limber.config.deserializer

import com.google.api.gax.rpc.NotFoundException
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import org.jetbrains.annotations.TestOnly

private typealias GcpSecretGetter = (id: String) -> String?

private val defaultGetter: GcpSecretGetter = { id ->
  SecretManagerServiceClient.create().use { client ->
    try {
      client.accessSecretVersion(SecretVersionName.parse(id)).payload.data.toStringUtf8()
    } catch (_: NotFoundException) {
      null
    }
  }
}

/**
 * Gets the value from GCP Secret Manager.
 */
internal object GcpSecretSource {
  private var delegate: GcpSecretGetter = defaultGetter

  operator fun get(name: String): String? =
    delegate(name)

  @TestOnly
  fun withOverride(get: (name: String) -> String?, block: () -> Unit) {
    delegate = get
    block()
    delegate = defaultGetter
  }
}
