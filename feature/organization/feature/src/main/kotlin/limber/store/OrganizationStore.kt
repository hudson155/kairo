package limber.store

import limber.exception.UnprocessableException
import limber.rep.OrganizationRep
import limber.sql.SqlStore
import limber.sql.Updater
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class OrganizationStore : SqlStore<OrganizationRep>(OrganizationRep::class) {
  fun create(model: OrganizationRep): OrganizationRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organization/create.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun get(guid: UUID): OrganizationRep? =
    get(guid, forUpdate = false)

  private fun get(guid: UUID, forUpdate: Boolean): OrganizationRep? =
    handle { handle ->
      val query = handle.createQuery(rs("store/organization/get.sql"))
      query.define("lockingClause", if (forUpdate) "for no key update" else "")
      query.bind("guid", guid)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun getByHostname(hostname: String): OrganizationRep? =
    handle { handle ->
      val query = handle.createQuery(rs("store/organization/getByHostname.sql"))
      query.bind("hostname", hostname)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun update(guid: UUID, updater: Updater<OrganizationRep>): OrganizationRep =
    transaction { handle ->
      val model = updater(get(guid, forUpdate = true) ?: organizationDoesNotExist())
      val query = handle.createQuery(rs("store/organization/update.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  private fun organizationDoesNotExist(): Nothing =
    throw UnprocessableException("Organization does not exist.")
}
