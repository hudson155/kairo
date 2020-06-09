package io.limberapp.backend.authorization.principal

import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.UUID
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import kotlinx.serialization.Serializable

@Serializable
data class JwtOrg(
  @Serializable(with = UuidSerializer::class)
  val guid: UUID,
  val name: String,
  val permissions: OrgPermissions,
  val features: Map<@Serializable(with = UuidSerializer::class) UUID, JwtFeature>
)
