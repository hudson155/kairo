package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

@Singleton
internal class FormInstanceStore @Inject constructor(private val jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: FormInstanceModel): FormInstanceModel {
    return jdbi.withHandle<FormInstanceModel, Exception> {
      it.createQuery(sqlResource("/store/formInstance/create.sql"))
        .bindKotlin(model)
        .mapTo(FormInstanceModel::class.java)
        .single()
    }
  }

  fun get(
    featureGuid: UUID? = null,
    formInstanceGuid: UUID? = null,
    creatorAccountGuid: UUID? = null
  ): Set<FormInstanceModel> {
    return jdbi.withHandle<Set<FormInstanceModel>, Exception> {
      it.createQuery("SELECT * FROM forms.form_instance WHERE <conditions>").build {
        if (featureGuid != null) {
          conditions.add("feature_guid = :featureGuid")
          bindings["featureGuid"] = featureGuid
        }
        if (formInstanceGuid != null) {
          conditions.add("guid = :formInstanceGuid")
          bindings["formInstanceGuid"] = formInstanceGuid
        }
        if (creatorAccountGuid != null) {
          conditions.add("creator_account_guid = :creatorAccountGuid")
          bindings["creatorAccountGuid"] = creatorAccountGuid
        }
      }
        .mapTo(FormInstanceModel::class.java)
        .toSet()
    }
  }

  fun update(formInstanceGuid: UUID, update: FormInstanceModel.Update): FormInstanceModel {
    return jdbi.inTransaction<FormInstanceModel, Exception> {
      val updateCount = it.createUpdate(sqlResource("/store/formInstance/update.sql"))
        .bind("guid", formInstanceGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FormInstanceNotFound()
        1 -> get(formInstanceGuid = formInstanceGuid).single()
        else -> badSql()
      }
    }
  }

  fun delete(formInstanceGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
        """
        DELETE
        FROM forms.form_instance
        WHERE guid = :guid
        """.trimIndent()
      )
        .bind("guid", formInstanceGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw FormInstanceNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
