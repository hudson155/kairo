package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleName

import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.OrgRoleAction
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.async
import io.limberapp.web.util.globalStyles
import io.limberapp.web.util.targetValue
import io.limberapp.web.util.useIsMounted
import kotlinx.css.Align
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.LinearDimension
import kotlinx.css.alignItems
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.fontSize
import kotlinx.css.marginRight
import kotlinx.css.px
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.dom.a
import react.dom.defaultValue
import react.dom.form
import react.dom.input
import react.dom.td
import react.functionalComponent
import react.useCallback
import react.useState
import styled.getClassName

/**
 * Portion of org roles table that shows the name of the org role.
 */
internal fun RBuilder.orgRolesTableRoleName(orgRole: OrgRoleRep.Complete) {
    child(component, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val styles = object : Styles("OrgRolesTableRoleName") {
    val form by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        alignItems = Align.center
        marginRight = 16.px
    }
    val input by css {
        flexGrow = 1.0
        fontSize = LinearDimension.initial
    }
    val icon by css {
        cursor = Cursor.pointer
    }
}.apply { inject() }

private enum class State { DISPLAYING, EDITING, SAVING }

private val component = functionalComponent<Props> { props ->
    val api = useApi()
    val global = useGlobalState()
    val isMounted = useIsMounted()

    val (state, setState) = useState(State.DISPLAYING)
    val (editValue, setValue) = useState(props.orgRole.name)

    val orgGuid = checkNotNull(global.state.org.state).guid

    val onEditClicked = useCallback<(Event) -> Unit>({
        setState(State.EDITING)
    }, emptyArray())
    val onCancelEdit = useCallback<(Event) -> Unit>({
        setValue(props.orgRole.name)
        setState(State.DISPLAYING)
    }, arrayOf(props.orgRole.name))
    val onSubmit = useCallback<(Event) -> Unit>({ event ->
        event.preventDefault()
        setState(State.SAVING)
        async {
            val orgRole = api.orgRoles(
                endpoint = OrgRoleApi.Patch(
                    orgGuid = orgGuid,
                    orgRoleGuid = props.orgRole.guid,
                    rep = OrgRoleRep.Update(name = editValue)
                )
            )
            global.dispatch(OrgRoleAction.UpdateValue(orgRole))
            if (isMounted.current) setState(State.DISPLAYING)
        }
    }, arrayOf(props.orgRole.guid, editValue))

    td {
        form(classes = styles.getClassName { it::form }) {
            attrs.onSubmitFunction = onSubmit
            when (state) {
                State.DISPLAYING -> +props.orgRole.name
                State.EDITING, State.SAVING -> {
                    input(type = InputType.text, classes = styles.getClassName { it::input }) {
                        attrs.autoFocus = true
                        attrs.defaultValue = editValue
                        attrs.onChangeFunction = { setValue(it.targetValue) }
                        attrs.disabled = state == State.SAVING
                    }
                }
            }
            when (state) {
                State.DISPLAYING -> {
                    a(classes = styles.getClassName { it::icon }) {
                        attrs.onClickFunction = onEditClicked
                        inlineIcon("edit")
                    }
                }
                State.EDITING -> {
                    a(classes = styles.getClassName { it::icon }) {
                        attrs.onClickFunction = onCancelEdit
                        inlineIcon("times-circle")
                    }
                    a(classes = styles.getClassName { it::icon }) {
                        attrs.onClickFunction = onSubmit
                        inlineIcon("save")
                    }
                }
                State.SAVING -> inlineIcon("spinner", classes = globalStyles.getClassName { it::spinner })
            }
        }
    }
}
