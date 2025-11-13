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

  override val koinModules: List<Module> =
    listOf(
      module {
        single {
          val mailersend = MailerSend().apply {
            @OptIn(ProtectedString.Access::class)
            token = config.apiToken.value
          }
          return@single Mailer(
            mailersend = mailersend,
            templates = config.templates,
          )
        }
      },
    )
}
