package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

internal class FormTemplateStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: FormTemplateModel): FormTemplateModel {
    return jdbi.withHandle<FormTemplateModel, Exception> {
      it.createQuery(sqlResource("create"))
        .bindKotlin(model)
        .mapTo(FormTemplateModel::class.java)
        .single()
    }
  }

  fun existsAndHasFeatureGuid(formTemplateGuid: UUID, featureGuid: UUID): Boolean {
    val model = get(formTemplateGuid) ?: return false
    return model.featureGuid == featureGuid
  }

  fun get(formTemplateGuid: UUID): FormTemplateModel? {
    return jdbi.withHandle<FormTemplateModel?, Exception> {
      it.createQuery("SELECT * FROM forms.form_template WHERE guid = :guid AND archived_date IS NULL")
        .bind("guid", formTemplateGuid)
        .mapTo(FormTemplateModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun getByFeatureGuid(featureGuid: UUID): Set<FormTemplateModel> {
    return jdbi.withHandle<Set<FormTemplateModel>, Exception> {
      it.createQuery(
        """
        SELECT *
        FROM forms.form_template
        WHERE feature_guid = :featureGuid
          AND archived_date IS NULL
        """.trimIndent()
      )
        .bind("featureGuid", featureGuid)
        .mapTo(FormTemplateModel::class.java)
        .toSet()
    }
  }

  fun update(formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel {
    return jdbi.inTransaction<FormTemplateModel, Exception> {
      val updateCount = it.createUpdate(sqlResource("update"))
        .bind("guid", formTemplateGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FormTemplateNotFound()
        1 -> get(formTemplateGuid)
        else -> badSql()
      }
    }
  }

  fun delete(formTemplateGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
        """
        UPDATE forms.form_template
        SET archived_date = NOW()
        WHERE guid = :guid
          AND archived_date IS NULL
        """.trimIndent()
      )
        .bind("guid", formTemplateGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw FormTemplateNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
