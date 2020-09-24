package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.components.formInstancesTable

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.common.types.UUID
import io.limberapp.common.util.date.prettyRelative
import io.limberapp.web.app.components.limberTable.components.limberTableCell.limberTableCell
import io.limberapp.web.app.components.limberTable.components.limberTableRow.limberTableRow
import io.limberapp.web.app.components.limberTable.limberTable
import io.limberapp.web.app.components.memberRow.memberRow
import io.limberapp.web.state.state.formTemplates.useFormTemplatesState
import io.limberapp.web.state.state.users.useUsersState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import io.limberapp.web.util.xs
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import styled.getClassName

internal fun RBuilder.formInstancesTable(
  formInstances: Set<FormInstanceRep.Summary>,
  onRowClick: (UUID) -> Unit,
) {
  child(component, Props(formInstances, onRowClick))
}

internal data class Props(
  val formInstances: Set<FormInstanceRep.Summary>,
  val onRowClick: (UUID) -> Unit,
) : RProps

private class S : Styles("FormInstancesTable") {
  val root by css {
    backgroundColor = Theme.Color.Background.white
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = Theme.Sizing.borderRadius
    marginTop = 10.px
    padding(20.px)
  }
  val row by css {
    xs {
      display = Display.flex
      flexDirection = FlexDirection.row
      flexWrap = FlexWrap.wrap
      justifyContent = JustifyContent.spaceBetween
      alignItems = Align.center
    }
  }
  val cell by css {
    xs {
      display = Display.inlineBlock
    }
  }
  val cellBreak by css {
    xs {
      display = Display.block
      width = 100.pct
      padding(vertical = 0.px)
    }
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (formTemplates, _) = useFormTemplatesState()
  val (users, _) = useUsersState()

  if (props.formInstances.isEmpty()) {
    p { +"No forms exist." }
    return
  }

  div(classes = s.c { it::root }) {
    limberTable(headers = listOf("#", "Submitted", null, "Type", "Creator")) {
      // TODO: Sort by unique sort key
      props.formInstances.forEach { formInstance ->
        limberTableRow(classes = s.c { it::row }, onClick = { props.onRowClick(formInstance.guid) }) {
          attrs.key = formInstance.guid
          limberTableCell(classes = s.c { it::cell }) {
            val number = formInstance.number?.toString() ?: ""
            span(classes = gs.getClassName { it::visibleXs }) { small { +"#$number" } }
            span(classes = gs.getClassName { it::hiddenXs }) { +number }
          }
          limberTableCell(classes = s.c { it::cell }) {
            formInstance.submittedDate?.prettyRelative()?.let { submittedDate ->
              span(classes = gs.getClassName { it::visibleXs }) { small { +submittedDate } }
              span(classes = gs.getClassName { it::hiddenXs }) { +submittedDate }
            }
          }
          limberTableCell(hideContent = true, classes = s.c { it::cellBreak }) { }
          limberTableCell(classes = s.c { it::cell }) {
            formTemplates[formInstance.formTemplateGuid]?.title?.let { +it }
          }
          limberTableCell(classes = s.c { it::cell }) {
            users[formInstance.creatorAccountGuid]?.let {
              memberRow(it, small = true, hideNameXs = true)
            }
          }
        }
      }
    }
  }
}
