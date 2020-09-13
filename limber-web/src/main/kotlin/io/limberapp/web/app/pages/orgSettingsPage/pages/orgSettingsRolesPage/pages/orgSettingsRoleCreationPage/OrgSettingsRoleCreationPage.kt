package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleCreationPage

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.modal.components.modalTitle.modalTitle
import io.limberapp.web.app.components.modal.modal
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import io.limberapp.web.state.state.orgRoles.useOrgRolesState
import io.limberapp.web.util.Page
import io.limberapp.web.util.Styles
import io.limberapp.web.util.async
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import io.limberapp.web.util.targetValue
import io.limberapp.web.util.useIsMounted
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.orgSettingsRoleCreationPage() {
  child(component)
}

internal typealias Props = RProps

internal object OrgSettingsRoleCreationPage : Page {
  const val path = "${OrgSettingsRolesPage.path}/create"
}

private class S : Styles("OrgSettingsRoleCreationPage") {
  val form by css {
    marginBottom = 24.px
  }
  val input by css {
    fontSize = LinearDimension.initial
    marginTop = 6.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val history = useHistory()
  val isMounted = useIsMounted()

  val (_, orgRolesMutator) = useOrgRolesState()

  val (newRoleName, setNewRoleName) = useState("")
  val (isSaving, setIsSaving) = useState(false)

  val goBack = { history.goBack() }

  val onCreate = {
    setIsSaving(true)
    async {
      orgRolesMutator.post(OrgRoleRep.Creation(newRoleName, OrgPermissions.none(), isDefault = false))
      if (isMounted.current) {
        goBack()
        setIsSaving(false)
      }
    }
  }

  orgSettingsRolesListPage() // This page is a modal over the list page, so render the list page.

  modal(onClose = goBack) {
    modalTitle("Create a new role")
    form(classes = s.c { it::form }) {
      label { +"Role name:" }
      br {}
      input(
        type = InputType.text,
        classes = s.c { it::input }) {
        attrs.autoFocus = true
        attrs.placeholder = "Administrators"
        attrs.onChangeFunction = { setNewRoleName(it.targetValue) }
      }
    }
    div(classes = gs.c { it::modalFooter }) {
      limberButton(
        style = Style.PRIMARY,
        loading = isSaving,
        onClick = onCreate
      ) { +"Create role" }
      limberButton(
        style = Style.SECONDARY,
        loading = isSaving,
        onClick = goBack
      ) { +"Cancel" }
    }
  }
}
