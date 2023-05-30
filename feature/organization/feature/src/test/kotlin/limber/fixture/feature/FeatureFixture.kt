package limber.fixture.feature

import limber.api.feature.FeatureApi
import limber.rep.feature.FeatureRep
import limber.testing.IntegrationTest

internal abstract class FeatureFixture {
  abstract val creator: FeatureRep.Creator
  abstract operator fun invoke(organizationId: String, id: String, isDefault: Boolean = false): FeatureRep

  internal companion object {
    val home: FeatureFixture = object : FeatureFixture() {
      override val creator: FeatureRep.Creator =
        FeatureRep.Creator(
          type = FeatureRep.Type.Placeholder,
          name = " Home ",
          iconName = " home ",
          rootPath = " /Home ",
        )

      override fun invoke(organizationId: String, id: String, isDefault: Boolean): FeatureRep =
        FeatureRep(
          id = id,
          organizationId = organizationId,
          isDefault = isDefault,
          type = FeatureRep.Type.Placeholder,
          name = "Home",
          iconName = "home",
          rootPath = "/home",
        )
    }

    val myForms: FeatureFixture = object : FeatureFixture() {
      override val creator: FeatureRep.Creator =
        FeatureRep.Creator(
          type = FeatureRep.Type.Form,
          name = " My forms ",
          iconName = null,
          rootPath = " /Forms ",
        )

      override fun invoke(organizationId: String, id: String, isDefault: Boolean): FeatureRep =
        FeatureRep(
          id = id,
          organizationId = organizationId,
          isDefault = isDefault,
          type = FeatureRep.Type.Form,
          name = "My forms",
          iconName = null,
          rootPath = "/forms",
        )
    }
  }
}

internal suspend fun IntegrationTest.create(
  organizationId: String,
  fixture: FeatureFixture,
): FeatureRep =
  featureClient(FeatureApi.Create(organizationId, fixture.creator))
