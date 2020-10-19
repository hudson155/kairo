package io.limberapp.graphqlServer.config

import io.limberapp.common.config.Config
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.authentication.ClockConfig
import io.limberapp.common.config.authentication.UuidsConfig

data class LimberGraphqlServerConfig(
    override val monolithBaseUrl: String,
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    override val uuids: UuidsConfig,
) : Config
