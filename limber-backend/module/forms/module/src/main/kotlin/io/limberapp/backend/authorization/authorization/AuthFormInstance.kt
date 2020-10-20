package io.limberapp.backend.authorization.authorization

import io.limberapp.backend.authorization.Auth
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.permissions.featurePermissions.feature.forms.FormsFeaturePermission

class AuthFormInstance(
    private val formInstance: FormInstanceModel,
    private val ifIsOwnFormInstance: FormsFeaturePermission?,
    private val ifIsNotOwnFormInstance: FormsFeaturePermission?,
) : Auth() {
  override fun authorizeInternal(jwt: Jwt?): Boolean {
    val isOwnFormInstance = formInstance.creatorAccountGuid == jwt?.user?.guid
    return Conditional(
        on = isOwnFormInstance,
        ifTrue = Conditional(
            on = formInstance.isDraft,
            ifTrue = Allow,
            ifFalse = ifIsOwnFormInstance?.let {
              AuthFeatureMember(formInstance.featureGuid, permission = it)
            } ?: Allow,
        ),
        ifFalse = Conditional(
            on = formInstance.isDraft,
            ifTrue = Deny,
            ifFalse = ifIsNotOwnFormInstance?.let {
              AuthFeatureMember(
                  featureGuid = formInstance.featureGuid,
                  permission = ifIsNotOwnFormInstance,
              )
            } ?: Deny,
        ),
    ).authorize(jwt)
  }
}
