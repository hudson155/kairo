package limber.feature.auth0

import limber.config.auth0.Auth0Config

public object TestAuth0Feature : Auth0Feature(
  config = Auth0Config(Auth0Config.ManagementApi.Fake),
)
