package kairo.mailersend

import com.mailersend.sdk.MailerSend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class Mailer(
  private val mailersend: MailerSend,
  public val templates: Map<String, String>,
) {
  public suspend fun use(block: MailerSend.() -> Unit) {
    withContext(Dispatchers.IO) {
      mailersend.block()
    }
  }
}
