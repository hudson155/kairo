package io.limberapp.module.graphql.config

import io.limberapp.config.Config
import io.limberapp.config.authentication.AuthenticationConfig
import io.limberapp.config.authentication.ClockConfig
import io.limberapp.config.authentication.UuidsConfig

internal data class GraphqlModuleConfig(
    override val monolithBaseUrl: String,
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    override val uuids: UuidsConfig,
) : Config
