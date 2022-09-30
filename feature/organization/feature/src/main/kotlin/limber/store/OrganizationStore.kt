package limber.store

import com.google.inject.Inject
import com.google.inject.Singleton
import limber.rep.OrganizationRep
import limber.rest.exception.UnprocessableException
import limber.sql.SqlStore
import limber.sql.handle
import limber.sql.transaction
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

@Singleton
internal class OrganizationStore @Inject constructor() : SqlStore() {
  fun create(creator: OrganizationRep): OrganizationRep =
    jdbi.transaction { handle ->
      val query = handle.createQuery(rs("store/organization/create.sql"))
      query.bindKotlin(creator)
      return@transaction query.mapTo(OrganizationRep::class.java).single()
    }

  fun get(organizationGuid: UUID): OrganizationRep? =
    jdbi.handle { handle ->
      val query = handle.createQuery(rs("store/organization/get.sql"))
      query.bind("organizationGuid", organizationGuid)
      return@handle query.mapTo(OrganizationRep::class.java).singleNullOrThrow()
    }

  fun update(organizationGuid: UUID, updater: OrganizationRep.Updater): OrganizationRep =
    jdbi.transaction { handle ->
      val query = handle.createQuery(rs("store/organization/update.sql"))
      query.bind("organizationGuid", organizationGuid)
      query.bindKotlin(updater)
      return@transaction query.mapTo(OrganizationRep::class.java).singleNullOrThrow()
        ?: throw UnprocessableException()
    }
}
