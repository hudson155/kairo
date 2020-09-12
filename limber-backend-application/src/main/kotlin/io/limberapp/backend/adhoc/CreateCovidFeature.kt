@file:Suppress("UNREACHABLE_CODE")

package io.limberapp.backend.adhoc

import com.google.inject.Injector
import com.piperframework.shutDown
import io.ktor.application.Application
import io.limberapp.backend.BaseLimberApp
import io.limberapp.backend.LimberModule
import io.limberapp.backend.config.LimberConfigLoader
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import java.time.LocalDateTime
import java.util.*

private object CreateCovidFeatureArgs {
  val orgGuid: UUID get() = TODO()
}

internal fun Adhoc.createCovidFeature() {
  val config = LimberConfigLoader().load()

  object : BaseLimberApp(application, config) {
    override fun getMainModules(application: Application) =
      super.getMainModules(application) + LimberSqlModule(config.sqlDatabase, runMigrations = false)

    override val modules = allLimberModules()

    override fun Application.afterStart(context: Context) {
      val featureGuid = createFeature(context.injector)
      createPreScreeningSelfAssessmentFormTemplate(context.injector, featureGuid)
      shutDown(0)
    }

    @OptIn(LimberModule.Orgs::class)
    private fun createFeature(injector: Injector): UUID {
      val featureService = injector.getInstance(FeatureService::class.java)
      return featureService.create(FeatureModel(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(),
        orgGuid = CreateCovidFeatureArgs.orgGuid,
        rank = 0,
        name = "COVID-19",
        path = "/covid",
        type = FeatureModel.Type.FORMS,
        isDefaultFeature = true,
      )).guid
    }

    @Suppress("LongMethod", "MaxLineLength")
    @OptIn(LimberModule.Forms::class)
    private fun createPreScreeningSelfAssessmentFormTemplate(injector: Injector, featureGuid: UUID) {
      val formTemplateService = injector.getInstance(FormTemplateService::class.java)
      val formTemplateQuestionService = injector.getInstance(FormTemplateQuestionService::class.java)
      val formTemplateGuid = formTemplateService.create(FormTemplateModel(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(),
        featureGuid = featureGuid,
        title = "Pre-screening self-assessment",
        description = null,
      )).guid
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(),
        formTemplateGuid = formTemplateGuid,
        label = """Are you experiencing any of the following:
          - severe difficulty breathing (e.g., struggling for each breath, speaking in single words)
          - severe chest pain
          - having a very hard time waking up
          - feeling confused
          - lost consciousness""".trimIndent(),
        helpText = null,
        required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(),
        formTemplateGuid = formTemplateGuid,
        label = """Are you experiencing any of the following:
          - shortness of breath at rest
          - inability to lie down because of difficulty breathing
          - chronic health conditions that you are having difficulty managing because of your current respiratory illness""".trimIndent(),
        helpText = null,
        required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(),
        formTemplateGuid = formTemplateGuid,
        label = """In the past 10 days, have you experienced any of the following:
          - fever
          - new onset of cough or worsening of chronic cough
          - new or worsening shortness of breath
          - new or worsening difficulty breathing
          - sore throat
          - runny nose""".trimIndent(),
        helpText = null,
        required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(),
        formTemplateGuid = formTemplateGuid,
        label = """Do you have any of the following:
          - chills
          - painful swallowing
          - stuffy nose
          - headache
          - muscle or joint ache
          - feeling unwell, fatigue or severe exhaustion
          - nausea, vomiting, diarrhea or unexplained loss of appetite
          - loss of sense of smell or taste
          - conjunctivitis (pink eye)""".trimIndent(),
        helpText = null,
        required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(),
        formTemplateGuid = formTemplateGuid,
        label = "In the past 14 days, did you return from travel outside of Canada, or did you have close contact with someone who is confirmed as having COVID-19?",
        helpText = null,
        required = true,
      ))
    }
  }
}
