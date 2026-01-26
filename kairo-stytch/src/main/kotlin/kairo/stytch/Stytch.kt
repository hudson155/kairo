package kairo.stytch

import com.stytch.java.consumer.StytchClient
import com.stytch.java.consumer.api.connectedapps.ConnectedApp
import com.stytch.java.consumer.api.cryptowallets.CryptoWallets
import com.stytch.java.consumer.api.debug.Debug
import com.stytch.java.consumer.api.fraud.Fraud
import com.stytch.java.consumer.api.idp.IDP
import com.stytch.java.consumer.api.impersonation.Impersonation
import com.stytch.java.consumer.api.m2m.M2M
import com.stytch.java.consumer.api.magiclinks.MagicLinks
import com.stytch.java.consumer.api.oauth.OAuth
import com.stytch.java.consumer.api.otp.OTPs
import com.stytch.java.consumer.api.passwords.Passwords
import com.stytch.java.consumer.api.project.Project
import com.stytch.java.consumer.api.rbac.RBAC
import com.stytch.java.consumer.api.sessions.Sessions
import com.stytch.java.consumer.api.totps.TOTPs
import com.stytch.java.consumer.api.users.Users
import com.stytch.java.consumer.api.webauthn.WebAuthn

/**
 * This wrapper is necessary for testing to work properly,
 * because [StytchClient] uses [JvmField].
 * https://github.com/stytchauth/stytch-java/issues/138
 */
public class Stytch(
  private val stytchClient: StytchClient,
) {
  public val connectedApp: ConnectedApp by lazy { stytchClient.connectedApp }

  public val cryptoWallets: CryptoWallets by lazy { stytchClient.cryptoWallets }

  public val debug: Debug by lazy { stytchClient.debug }

  public val fraud: Fraud by lazy { stytchClient.fraud }

  public val idp: IDP by lazy { stytchClient.idp }

  public val impersonation: Impersonation by lazy { stytchClient.impersonation }

  public val m2m: M2M by lazy { stytchClient.m2m }

  public val magicLinks: MagicLinks by lazy { stytchClient.magicLinks }

  public val oauth: OAuth by lazy { stytchClient.oauth }

  public val otps: OTPs by lazy { stytchClient.otps }

  public val passwords: Passwords by lazy { stytchClient.passwords }

  public val project: Project by lazy { stytchClient.project }

  public val rbac: RBAC by lazy { stytchClient.rbac }

  public val sessions: Sessions by lazy { stytchClient.sessions }

  public val totps: TOTPs by lazy { stytchClient.totps }

  public val users: Users by lazy { stytchClient.users }

  public val webauthn: WebAuthn by lazy { stytchClient.webauthn }
}
