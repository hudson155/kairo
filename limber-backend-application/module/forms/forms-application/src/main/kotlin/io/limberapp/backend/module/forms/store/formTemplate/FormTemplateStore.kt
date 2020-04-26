package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jetbrains.exposed.sql.Database
import java.util.UUID

internal class FormTemplateStore @Inject constructor(
    database: Database,
    private val jdbi: Jdbi,
    private val formTemplateQuestionStore: FormTemplateQuestionStore
) : SqlStore(database) {
    fun create(model: FormTemplateModel) {
        jdbi.useTransaction<Exception> {
            it.createUpdate(sqlResource("create")).bindKotlin(model).execute()
            formTemplateQuestionStore.create(model.guid, model.questions)
        }
    }

    fun get(formTemplateGuid: UUID): FormTemplateModel? {
        return jdbi.withHandle<FormTemplateModel?, Exception> {
            it.createQuery("SELECT * FROM forms.form_template WHERE guid = :guid")
                .bind("guid", formTemplateGuid)
                .mapTo(FormTemplateModel::class.java)
                .singleNullOrThrow()
                ?.copy(questions = formTemplateQuestionStore.getByFormTemplateGuid(formTemplateGuid))
        }
    }

    fun getByFeatureGuid(featureGuid: UUID): Set<FormTemplateModel> {
        return jdbi.withHandle<Set<FormTemplateModel>, Exception> {
            it.createQuery("SELECT * FROM forms.form_template WHERE feature_guid = :featureGuid")
                .bind("featureGuid", featureGuid)
                .mapTo(FormTemplateModel::class.java)
                .map { it.copy(questions = formTemplateQuestionStore.getByFormTemplateGuid(it.guid)) }
                .toSet()
        }
    }

    fun update(formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel {
        return jdbi.inTransaction<FormTemplateModel, Exception> {
            val updateCount = it.createUpdate(sqlResource("update"))
                .bind("guid", formTemplateGuid)
                .bindKotlin(update)
                .execute()
            when (updateCount) {
                0 -> throw FormTemplateNotFound()
                1 -> return@inTransaction get(formTemplateGuid)
                else -> badSql()
            }
        }
    }

    fun delete(formTemplateGuid: UUID) {
        jdbi.useTransaction<Exception> {
            val updateCount = it.createUpdate("DELETE FROM forms.form_template WHERE guid = :guid")
                .bind("guid", formTemplateGuid)
                .execute()
            when (updateCount) {
                0 -> throw FormTemplateNotFound()
                1 -> return@useTransaction
                else -> badSql()
            }
        }
    }
}
