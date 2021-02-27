package io.limberapp.mapper.user

import com.google.inject.Inject
import io.limberapp.model.user.UserModel
import io.limberapp.permissions.limber.LimberPermissions
import io.limberapp.rep.user.UserRep
import io.limberapp.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZonedDateTime

internal class UserMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
) {
  fun model(rep: UserRep.Creation): UserModel =
      UserModel(
          guid = uuidGenerator.generate(),
          createdDate = ZonedDateTime.now(clock),
          permissions = LimberPermissions.none(),
          orgGuid = rep.orgGuid,
          emailAddress = rep.emailAddress,
          firstName = rep.firstName,
          lastName = rep.lastName,
          profilePhotoUrl = rep.profilePhotoUrl,
      )

  fun summaryRep(model: UserModel): UserRep.Summary =
      UserRep.Summary(
          guid = model.guid,
          orgGuid = model.orgGuid,
          firstName = model.firstName,
          lastName = model.lastName,
          fullName = model.fullName,
          profilePhotoUrl = model.profilePhotoUrl,
      )

  fun completeRep(model: UserModel): UserRep.Complete =
      UserRep.Complete(
          guid = model.guid,
          permissions = model.permissions,
          orgGuid = model.orgGuid,
          emailAddress = model.emailAddress,
          firstName = model.firstName,
          lastName = model.lastName,
          fullName = model.fullName,
          profilePhotoUrl = model.profilePhotoUrl,
      )

  fun update(rep: UserRep.Update): UserModel.Update =
      UserModel.Update(
          permissions = rep.permissions,
          firstName = rep.firstName,
          lastName = rep.lastName,
      )
}
