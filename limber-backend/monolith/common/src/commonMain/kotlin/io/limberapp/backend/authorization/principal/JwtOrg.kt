package io.limberapp.backend.authorization.principal

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.UUID
import kotlinx.serialization.Serializable

@Serializable
data class JwtOrg(
  @Serializable(with = UuidSerializer::class)
  val guid: UUID,
  val name: String,
  val isOwner: Boolean,
  val permissions: OrgPermissions,
  val features: Map<@Serializable(with = UuidSerializer::class) UUID, JwtFeature>,
)
