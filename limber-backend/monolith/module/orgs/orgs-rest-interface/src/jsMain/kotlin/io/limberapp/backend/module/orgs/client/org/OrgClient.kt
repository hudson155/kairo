package io.limberapp.backend.module.orgs.client.org

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.common.util.Outcome

interface OrgClient {
  suspend operator fun invoke(endpoint: OrgApi.Get): Outcome<OrgRep.Complete>

  suspend operator fun invoke(endpoint: OrgApi.Patch): Outcome<OrgRep.Complete>
}
