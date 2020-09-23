package io.limberapp.monolith.adhoc

import com.google.inject.Injector
import io.ktor.application.Application
import io.limberapp.backend.LimberModule
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.module.SqlWrapper
import io.limberapp.common.shutDown
import io.limberapp.config.ConfigLoader
import io.limberapp.monolith.BaseLimberApp
import io.limberapp.monolith.config.LimberMonolithConfig
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

internal fun Adhoc.dbReset() {
  val config = ConfigLoader.load<LimberMonolithConfig>(System.getenv("LIMBER_CONFIG"))

  with(SqlWrapper(config.sqlDatabase)) {
    connect()
    with(checkNotNull(dataSource).connection) {
      createStatement().execute("DROP SCHEMA IF EXISTS auth, forms, orgs, users CASCADE")
      createStatement().execute("DROP TABLE IF EXISTS flyway_schema_history")
    }
    disconnect()
  }

  object : BaseLimberApp(application, config) {
    override fun getApplicationModules() = allLimberModules()

    override fun getAdditionalModules() = listOf(LimberSqlModule(config.sqlDatabase, runMigrations = true))

    override fun afterStart(application: Application, injector: Injector) {
      insertFixtures(injector)
      application.shutDown(0)
    }

    @Suppress("LongMethod")
    @OptIn(
      LimberModule.Auth::class,
      LimberModule.Forms::class,
      LimberModule.Orgs::class,
      LimberModule.Users::class,
    )
    private fun insertFixtures(injector: Injector) {
      // Create Limber org and tenant.

      val orgService = injector.getInstance(OrgService::class.java)

      val org = orgService.create(OrgModel(
        guid = UUID.fromString("46c25689-71d6-4544-8c8c-6936d761dddb"),
        createdDate = LocalDateTime.now(),
        name = "Limber",
        ownerUserGuid = null,
      ))

      val orgRoleService = injector.getInstance(OrgRoleService::class.java)
      val featureService = injector.getInstance(FeatureService::class.java)
      val tenantService = injector.getInstance(TenantService::class.java)
      val tenantDomainService = injector.getInstance(TenantDomainService::class.java)

      orgRoleService.create(OrgRoleModel(
        guid = UUID.fromString("b9ce1942-897d-470f-8b68-822d901005a6"),
        createdDate = LocalDateTime.now(),
        orgGuid = org.guid,
        name = "Admins",
        permissions = OrgPermissions.fromBitString("1100"),
        isDefault = false,
        memberCount = 0,
      ))
      orgRoleService.create(OrgRoleModel(
        guid = UUID.fromString("e7b681af-812d-4999-a016-ebb3d6f23104"),
        createdDate = LocalDateTime.now(),
        orgGuid = org.guid,
        name = "Members",
        permissions = OrgPermissions.fromBitString("0010"),
        isDefault = true,
        memberCount = 0,
      ))

      featureService.create(FeatureModel(
        guid = UUID.fromString("75a2ed7a-4247-4e63-ab10-a60df3d9aeee"),
        createdDate = LocalDateTime.now(),
        orgGuid = org.guid,
        rank = 0,
        name = "Home",
        path = "/home",
        type = FeatureModel.Type.HOME,
        isDefaultFeature = true,
      ))
      val formsFeature = featureService.create(FeatureModel(
        guid = UUID.fromString("3dc95c5d-767c-4b29-9c50-a6f93edd0c06"),
        createdDate = LocalDateTime.now(),
        orgGuid = org.guid,
        rank = 1,
        name = "Forms",
        path = "/forms",
        type = FeatureModel.Type.FORMS,
        isDefaultFeature = false,
      ))

      tenantService.create(TenantModel(
        createdDate = LocalDateTime.now(),
        orgGuid = org.guid,
        auth0ClientId = "kwQNShDsyv7vAObSiBG12O46CMY7qr2Q",
      ))

      tenantDomainService.create(TenantDomainModel(
        createdDate = LocalDateTime.now(),
        orgGuid = org.guid,
        domain = "localhost:3000",
      ))
      tenantDomainService.create(TenantDomainModel(
        createdDate = LocalDateTime.now(),
        orgGuid = org.guid,
        domain = "localhost:8080",
      ))
      tenantDomainService.create(TenantDomainModel(
        createdDate = LocalDateTime.now(),
        orgGuid = org.guid,
        domain = "limber.limberapp.io",
      ))

      // Create user accounts.

      val userService = injector.getInstance(UserService::class.java)

      @Suppress("MaxLineLength")
      val jeffHudsonUser = userService.create(UserModel(
        guid = UUID.fromString("3e2d1681-a666-456e-a168-647d8c3a3150"),
        createdDate = LocalDateTime.now(),
        identityProvider = false,
        superuser = true,
        orgGuid = org.guid,
        firstName = "Jeff",
        lastName = "Hudson",
        emailAddress = "jeff.hudson@limberapp.io",
        profilePhotoUrl = "https://avatars0.githubusercontent.com/u/1360420?s=460&u=701cdf42f8638e94e4fd374efe5416638a608074&v=4",
      ))
      orgService.update(org.guid, OrgModel.Update(name = null, ownerUserGuid = jeffHudsonUser.guid))

      @Suppress("MaxLineLength")
      val noahGuldUser = userService.create(UserModel(
        guid = UUID.fromString("71fe66b2-f115-43b9-a993-cbeb51c1b46a"),
        createdDate = LocalDateTime.now(),
        identityProvider = false,
        superuser = true,
        orgGuid = org.guid,
        firstName = "Noah",
        lastName = "Guld",
        emailAddress = "noah.guld@limberapp.io",
        profilePhotoUrl = "https://avatars0.githubusercontent.com/u/8917186?s=460&u=364b0d5270cb9657b4222c0816713831805957c9&v=4",
      ))

      // Create vehicle inspection form.

      val formTemplateService = injector.getInstance(FormTemplateService::class.java)
      val formTemplateQuestionService = injector.getInstance(FormTemplateQuestionService::class.java)
      val formInstanceService = injector.getInstance(FormInstanceService::class.java)
      val formInstanceQuestionService = injector.getInstance(FormInstanceQuestionService::class.java)

      val vehicleInspectionFormTemplate = formTemplateService.create(FormTemplateModel(
        guid = UUID.fromString("85ae9d91-2666-4174-bdfe-1f7cd868869c"),
        createdDate = LocalDateTime.now(),
        featureGuid = formsFeature.guid,
        title = "Vehicle Inspection",
        description = null,
      ))
      val vehicleInspectionWorkerNameFormTemplateQuestion =
        formTemplateQuestionService.create(formsFeature.guid, FormTemplateTextQuestionModel(
          guid = UUID.fromString("59c0b82c-57d3-4d29-b7ef-0bbf90084d7d"),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = vehicleInspectionFormTemplate.guid,
          label = "Worker name",
          helpText = null,
          required = true,
          multiLine = false,
          placeholder = null,
          validator = null,
        ))
      val vehicleInspectionDateFormTemplateQuestion =
        formTemplateQuestionService.create(formsFeature.guid, FormTemplateDateQuestionModel(
          guid = UUID.fromString("2a355fc7-33a6-44af-b737-ffa874b2b1a0"),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = vehicleInspectionFormTemplate.guid,
          label = "Date",
          helpText = null,
          required = true,
          earliest = null,
          latest = null,
        ))
      val vehicleInspectionDescriptionFormTemplateQuestion =
        formTemplateQuestionService.create(formsFeature.guid, FormTemplateTextQuestionModel(
          guid = UUID.fromString("43f20f0d-03c0-4477-808d-8cfefefb46bc"),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = vehicleInspectionFormTemplate.guid,
          label = "Description",
          helpText = null,
          required = false,
          multiLine = true,
          placeholder = null,
          validator = null,
        ))
      formTemplateQuestionService.create(formsFeature.guid, FormTemplateRadioSelectorQuestionModel(
        guid = UUID.fromString("1dd86a5e-60bb-4257-9862-caa480ed5286"),
        createdDate = LocalDateTime.now(),
        formTemplateGuid = vehicleInspectionFormTemplate.guid,
        label = "Options",
        helpText = null,
        required = false,
        options = listOf("option 1", "option 2"),
      ))

      val preFlightSafetyInspectionFormTemplate = formTemplateService.create(FormTemplateModel(
        guid = UUID.fromString("3e8e966d-613f-4bf9-936a-2b1e3c6ab3f0"),
        createdDate = LocalDateTime.now(),
        featureGuid = formsFeature.guid,
        title = "Pre-Flight Safety Inspection",
        description = null,
      ))
      val preFlightSafetyInspectionPilotNameFormTemplateQuestion =
        formTemplateQuestionService.create(formsFeature.guid, FormTemplateTextQuestionModel(
          guid = UUID.fromString("50c32a31-35c6-458e-b582-f3f8c9a019e4"),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = preFlightSafetyInspectionFormTemplate.guid,
          label = "Pilot name",
          helpText = null,
          required = true,
          multiLine = false,
          placeholder = null,
          validator = Regex("[a-zA-Z]{2,}(?: [a-zA-Z]{2,}){0,2}"),
        ))
      val preFlightSafetyInspectionDepartureDateFormTemplateQuestion =
        formTemplateQuestionService.create(formsFeature.guid, FormTemplateDateQuestionModel(
          guid = UUID.fromString("283167ce-a9fe-4de5-9042-ac1707e8f8dc"),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = preFlightSafetyInspectionFormTemplate.guid,
          label = "Departure date",
          helpText = null,
          required = true,
          earliest = LocalDate.of(2020, 1, 1),
          latest = LocalDate.of(2021, 1, 1),
        ))
      formTemplateQuestionService.create(formsFeature.guid, FormTemplateTextQuestionModel(
        guid = UUID.fromString("761dea1a-e422-4e3c-81c6-497ec21cc1c3"),
        createdDate = LocalDateTime.now(),
        formTemplateGuid = preFlightSafetyInspectionFormTemplate.guid,
        label = "Destination",
        helpText = null,
        required = false,
        multiLine = false,
        placeholder = null,
        validator = null,
      ))
      val preFlightSafetyInspectionPlaneFormTemplateQuestion =
        formTemplateQuestionService.create(formsFeature.guid, FormTemplateRadioSelectorQuestionModel(
          guid = UUID.fromString("d9fbaa35-ffd6-4a66-8c26-7d40e32ac7aa"),
          createdDate = LocalDateTime.now(),
          formTemplateGuid = preFlightSafetyInspectionFormTemplate.guid,
          label = "Plane",
          helpText = null,
          required = false,
          options = listOf("HondaJet", "SR20", "Challenger"),
        ))

      val formInstance1 = formInstanceService.create(FormInstanceModel(
        guid = UUID.fromString("8e6ff3f9-26a0-42e4-935b-100c0327ab79"),
        createdDate = LocalDateTime.now(),
        featureGuid = formsFeature.guid,
        formTemplateGuid = vehicleInspectionFormTemplate.guid,
        number = 1,
        submittedDate = LocalDateTime.now(),
        creatorAccountGuid = noahGuldUser.guid,
      ))
      formInstanceQuestionService.upsert(formsFeature.guid, FormInstanceTextQuestionModel(
        createdDate = LocalDateTime.now(),
        formInstanceGuid = formInstance1.guid,
        questionGuid = vehicleInspectionWorkerNameFormTemplateQuestion.guid,
        text = "Kanye West",
      ))
      @Suppress("MagicNumber")
      formInstanceQuestionService.upsert(formsFeature.guid, FormInstanceDateQuestionModel(
        createdDate = LocalDateTime.now(),
        formInstanceGuid = formInstance1.guid,
        questionGuid = vehicleInspectionDateFormTemplateQuestion.guid,
        date = LocalDate.of(2020, 1, 28),
      ))
      formInstanceQuestionService.upsert(formsFeature.guid, FormInstanceTextQuestionModel(
        createdDate = LocalDateTime.now(),
        formInstanceGuid = formInstance1.guid,
        questionGuid = vehicleInspectionDescriptionFormTemplateQuestion.guid,
        text = "I did this vehicle inspection on my birthday!",
      ))

      val formInstance2 = formInstanceService.create(FormInstanceModel(
        guid = UUID.fromString("168af0c7-1ab1-40f6-9715-c9e8c241442f"),
        createdDate = LocalDateTime.now(),
        featureGuid = formsFeature.guid,
        formTemplateGuid = preFlightSafetyInspectionFormTemplate.guid,
        number = 2,
        submittedDate = LocalDateTime.now(),
        creatorAccountGuid = jeffHudsonUser.guid,
      ))
      formInstanceQuestionService.upsert(formsFeature.guid, FormInstanceTextQuestionModel(
        createdDate = LocalDateTime.now(),
        formInstanceGuid = formInstance2.guid,
        questionGuid = preFlightSafetyInspectionPilotNameFormTemplateQuestion.guid,
        text = "Summer Kavan",
      ))
      @Suppress("MagicNumber")
      formInstanceQuestionService.upsert(formsFeature.guid, FormInstanceDateQuestionModel(
        createdDate = LocalDateTime.now(),
        formInstanceGuid = formInstance2.guid,
        questionGuid = preFlightSafetyInspectionDepartureDateFormTemplateQuestion.guid,
        date = LocalDate.of(2020, 2, 1),
      ))
      formInstanceQuestionService.upsert(formsFeature.guid, FormInstanceRadioSelectorQuestionModel(
        createdDate = LocalDateTime.now(),
        formInstanceGuid = formInstance2.guid,
        questionGuid = preFlightSafetyInspectionPlaneFormTemplateQuestion.guid,
        selections = listOf("SR20"),
      ))
    }
  }
}
