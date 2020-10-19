package io.limberapp.module.graphql.config

import io.limberapp.common.config.Config
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.authentication.ClockConfig
import io.limberapp.common.config.authentication.UuidsConfig

internal data class GraphqlModuleConfig(
    override val monolithBaseUrl: String,
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    override val uuids: UuidsConfig,
) : Config
