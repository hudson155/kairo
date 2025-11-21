package kairo.mailersend

import com.mailersend.sdk.MailerSend
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.protectedString.ProtectedString
import org.koin.core.module.Module
import org.koin.dsl.module

public class MailersendFeature(
  config: MailersendFeatureConfig,
) : Feature(), HasKoinModules {
  override val name: String = "MailerSend"

  private val mailer: Mailer by lazy {
    val mailersend = MailerSend().apply {
      @OptIn(ProtectedString.Access::class)
      token = config.apiToken.value
    }
    return@lazy Mailer(
      mailersend = mailersend,
      templates = config.templates,
    )
  }

  override val koinModules: List<Module> =
    listOf(
      module {
        single { mailer }
      },
    )
}
