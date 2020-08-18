package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage

import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceAnswerPage.FormInstanceAnswerPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceAnswerPage.formInstanceAnswerPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.formInstancesListPage
import io.limberapp.web.auth.useAuth
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.state.state.formInstances.formInstancesStateProvider
import io.limberapp.web.state.state.user.useUserState
import io.limberapp.web.util.Page
import react.*
import react.router.dom.*

internal fun RBuilder.formInstancesPage() {
  child(component)
}

internal typealias Props = RProps

internal object FormInstancesPage : Page {
  const val subpath = "/instances"
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()
  val auth = useAuth()
  val match = checkNotNull(useRouteMatch<RProps>())

  val (feature, _) = useFeatureState()
  val (user, _) = useUserState()

  val formInstances = load {
    api(
      endpoint = FormInstanceApi.GetByFeatureGuid(
        featureGuid = feature.guid,
        creatorAccountGuid = run {
          // If the user has permission to see others' form instances, we'll request all form instances by using null
          // for the creator account GUID in the request. Otherwise, we'll only request the form instances created by
          // the current user.
          val permissions = checkNotNull(auth.featurePermissions[feature.guid])
          if (FormsFeaturePermission.SEE_OTHERS_FORM_INSTANCES in permissions) null else user.guid
        }
      )
    )
  }

  // While the form instances are loading, show a loading spinner.
  (formInstances ?: return loadingSpinner()).onFailure { return failedToLoad("form instances") }

  formInstancesStateProvider(formInstances.value.associateBy { it.guid }) {
    switch {
      route(path = match.path, exact = true) {
        buildElement { formInstancesListPage() }
      }
      route(path = match.path + FormInstanceAnswerPage.subpath, exact = true) {
        buildElement { formInstanceAnswerPage() }
      }
    }
  }
}
