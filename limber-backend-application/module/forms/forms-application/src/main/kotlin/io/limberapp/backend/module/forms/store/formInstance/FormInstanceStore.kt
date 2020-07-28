package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

internal class FormInstanceStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: FormInstanceModel): FormInstanceModel {
    return jdbi.withHandle<FormInstanceModel, Exception> {
      it.createQuery(sqlResource("/store/formInstance/create.sql"))
        .bindKotlin(model)
        .mapTo(FormInstanceModel::class.java)
        .single()
    }
  }

  fun existsAndHasFeatureGuid(formInstanceGuid: UUID, featureGuid: UUID): Boolean {
    val model = get(formInstanceGuid) ?: return false
    return model.featureGuid == featureGuid
  }

  fun get(formInstanceGuid: UUID): FormInstanceModel? {
    return jdbi.withHandle<FormInstanceModel?, Exception> {
      it.createQuery("SELECT * FROM forms.form_instance WHERE guid = :guid AND archived_date IS NULL")
        .bind("guid", formInstanceGuid)
        .mapTo(FormInstanceModel::class.java)
        .singleOrNull()
    }
  }

  fun getByFeatureGuid(featureGuid: UUID, creatorAccountGuid: UUID?): Set<FormInstanceModel> {
    return jdbi.withHandle<Set<FormInstanceModel>, Exception> {
      val (conditions, bindings) = conditionsAndBindings()

      conditions.add("feature_guid = :featureGuid")
      bindings["featureGuid"] = featureGuid

      if (creatorAccountGuid != null) {
        conditions.add("creator_account_guid = :creatorAccountGuid")
        bindings["creatorAccountGuid"] = creatorAccountGuid
      }

      it.createQuery(
        """
        SELECT *
        FROM forms.form_instance
        WHERE <conditions>
          AND archived_date IS NULL
        """.trimIndent()
      )
        .define("conditions", conditions.joinToString(" AND "))
        .bindMap(bindings)
        .mapTo(FormInstanceModel::class.java)
        .toSet()
    }
  }

  fun update(formInstanceGuid: UUID, update: FormInstanceModel.Update): FormInstanceModel {
    return jdbi.inTransaction<FormInstanceModel, Exception> {
      val updateCount = it.createUpdate(sqlResource("update"))
        .bind("guid", formInstanceGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FormInstanceNotFound()
        1 -> get(formInstanceGuid)
        else -> badSql()
      }
    }
  }

  fun delete(formInstanceGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
        """
        UPDATE forms.form_instance
        SET archived_date = NOW()
        WHERE guid = :guid
          AND archived_date IS NULL
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
