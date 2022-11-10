package limber.fixture.feature

import limber.api.feature.FeatureApi
import limber.rep.feature.FeatureRep
import limber.testing.IntegrationTest
import java.util.UUID

internal abstract class FeatureFixture {
  abstract val creator: FeatureRep.Creator
  abstract operator fun invoke(organizationGuid: UUID, guid: UUID): FeatureRep

  internal companion object {
    val home: FeatureFixture = object : FeatureFixture() {
      override val creator: FeatureRep.Creator =
        FeatureRep.Creator(type = FeatureRep.Type.Placeholder, name = " Home ", rootPath = " /Home ")

      override fun invoke(organizationGuid: UUID, guid: UUID): FeatureRep =
        FeatureRep(
          organizationGuid = organizationGuid,
          guid = guid,
          isDefault = false, // May need to be updated after returned.
          type = FeatureRep.Type.Placeholder,
          name = "Home",
          rootPath = "/home",
        )
    }

    val myForms: FeatureFixture = object : FeatureFixture() {
      override val creator: FeatureRep.Creator =
        FeatureRep.Creator(type = FeatureRep.Type.Form, name = " My forms ", rootPath = " /Forms ")

      override fun invoke(organizationGuid: UUID, guid: UUID): FeatureRep =
        FeatureRep(
          organizationGuid = organizationGuid,
          guid = guid,
          isDefault = false, // May need to be updated after returned.
          type = FeatureRep.Type.Form,
          name = "My forms",
          rootPath = "/forms",
        )
    }
  }
}

internal suspend fun IntegrationTest.create(
  organizationGuid: UUID,
  fixture: FeatureFixture,
): FeatureRep =
  featureClient(FeatureApi.Create(organizationGuid, fixture.creator))
