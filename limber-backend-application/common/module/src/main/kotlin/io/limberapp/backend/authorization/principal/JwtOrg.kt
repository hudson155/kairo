package io.limberapp.backend.authorization.principal

import com.piperframework.serialization.serializer.UuidSerializer
import io.limberapp.backend.authorization.permissions.OrgPermissions
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class JwtOrg(
  @Serializable(with = UuidSerializer::class)
  val guid: UUID,
  val name: String,
  val permissions: OrgPermissions,
  val featureGuids: List<@Serializable(with = UuidSerializer::class) UUID>
)
