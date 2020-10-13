@file:Suppress("UNREACHABLE_CODE")

package io.limberapp.monolith.adhoc

import com.google.inject.Injector
import io.ktor.application.Application
import io.limberapp.backend.LimberModule
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import io.limberapp.backend.module.auth.service.feature.FeatureRoleService
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.orgs.model.feature.FeatureModel
import io.limberapp.backend.module.orgs.service.feature.FeatureService
import io.limberapp.common.shutDown
import io.limberapp.config.ConfigLoader
import io.limberapp.monolith.BaseLimberApp
import io.limberapp.monolith.config.LimberMonolithConfig
import java.time.LocalDateTime
import java.util.*

private object CreateCovidFeatureArgs {
  val orgGuid: UUID get() = TODO()
  val membersOrgRoleGuid: UUID get() = TODO()
  val managersOrgRoleGuid: UUID get() = TODO()
}

internal fun Adhoc.createCovidFeature() {
  val config = ConfigLoader.load<LimberMonolithConfig>(System.getenv("LIMBER_CONFIG"))

  object : BaseLimberApp(application, config) {
    override fun getApplicationModules() = allLimberModules()

    override fun getAdditionalModules() = listOf(LimberSqlModule(config.sqlDatabase, runMigrations = false))

    override fun afterStart(application: Application, injector: Injector) {
      val featureGuid = createFeature(injector)
      createFeatureRoles(injector, featureGuid = featureGuid)
      createPreScreeningSelfAssessmentFormTemplate(injector, featureGuid = featureGuid)
      createWorkplaceInspectionFormTemplate(injector, featureGuid = featureGuid)
      application.shutDown(0)
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

    @OptIn(LimberModule.Auth::class)
    private fun createFeatureRoles(injector: Injector, featureGuid: UUID) {
      val featureRoleService = injector.getInstance(FeatureRoleService::class.java)
      featureRoleService.create(FeatureRoleModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          featureGuid = featureGuid,
          orgRoleGuid = CreateCovidFeatureArgs.membersOrgRoleGuid,
          permissions = FormsFeaturePermissions(setOf(FormsFeaturePermission.CREATE_FORM_INSTANCES)),
      ))
      featureRoleService.create(FeatureRoleModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          featureGuid = featureGuid,
          orgRoleGuid = CreateCovidFeatureArgs.managersOrgRoleGuid,
          permissions = FormsFeaturePermissions(setOf(
              FormsFeaturePermission.SEE_OTHERS_FORM_INSTANCES,
              FormsFeaturePermission.EXPORT_FORM_INSTANCES,
          )),
      ))
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

    @Suppress("LongMethod", "MaxLineLength")
    @OptIn(LimberModule.Forms::class)
    private fun createWorkplaceInspectionFormTemplate(injector: Injector, featureGuid: UUID) {
      val formTemplateService = injector.getInstance(FormTemplateService::class.java)
      val formTemplateQuestionService = injector.getInstance(FormTemplateQuestionService::class.java)
      val formTemplateGuid = formTemplateService.create(FormTemplateModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          featureGuid = featureGuid,
          title = "Workplace inspection",
          description = null,
      )).guid
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Has the COVID-19 Policy been communicated?",
          helpText = "e.g. Through visible signage, via email, discussed in meetings, etc.",
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Have the COVID-19 social distancing requirements been communicated?",
          helpText = "e.g. Through visible signage, via email, discussed in meetings, etc.",
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Has it been communicated that anyone experiencing symptoms of COVID-19 is not allowed on the" +
              " premises (unless symptoms are related to known, unrelated health conditions)?",
          helpText = "e.g. Through pre-screening self-assessment.",
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Is hand sanitizer readily available?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Are cleaning and sanitization supplies available?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Is the work area clean, sanitized and free of debris?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Have high touch areas been cleaned and sanitized?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Have equipment and tools been cleaned and sanitized?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Have all non-food contact surfaces been disinfected?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Have all food contact surfaces been disinfected?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Are all staff wearing required PPE?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateYesNoQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Is social distancing practiced?",
          helpText = null,
          required = true,
      ))
      formTemplateQuestionService.create(featureGuid, FormTemplateTextQuestionModel(
          guid = UUID.randomUUID(),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = formTemplateGuid,
          label = "Other observations and comments:",
          helpText = null,
          required = false,
          multiLine = true,
          placeholder = null,
          validator = null,
      ))
    }
  }
}
