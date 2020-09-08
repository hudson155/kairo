package io.limberapp.backend

import com.google.inject.Injector
import com.piperframework.SimplePiperApp
import com.piperframework.ktorAuth.piperAuth
import com.piperframework.module.MainModule
import com.piperframework.module.ModuleWithLifecycle
import io.ktor.application.Application
import io.ktor.auth.Authentication
import io.limberapp.backend.authentication.jwt.JwtAuthVerifier
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.config.LimberAppMonolithConfig
import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.forms.FormsModule
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.module.users.UsersModule

internal abstract class BaseLimberApp(
  application: Application,
  config: LimberAppMonolithConfig,
) : SimplePiperApp<LimberAppMonolithConfig>(application, config) {
  final override fun Authentication.Configuration.configureAuthentication(injector: Injector) {
    piperAuth<Jwt> {
      verifier(
        scheme = JwtAuthVerifier.scheme,
        verifier = JwtAuthVerifier(config.authentication),
        default = true,
      )
    }
  }

  override fun getMainModules(application: Application) = listOf<ModuleWithLifecycle>(
    MainModule.forProduction(application, config)
  )

  protected fun allLimberModules() = listOf(
    AuthModule(),
    FormsModule(),
    OrgsModule(),
    UsersModule(),
  )
}
