package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.components.formInstancesTable

import com.piperframework.types.UUID
import com.piperframework.util.prettyRelative
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.app.components.limberTable.components.limberTableCell.limberTableCell
import io.limberapp.web.app.components.limberTable.components.limberTableRow.limberTableRow
import io.limberapp.web.app.components.limberTable.limberTable
import io.limberapp.web.app.components.memberRow.memberRow
import io.limberapp.web.context.globalState.action.users.loadUsers
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.componentWithApi
import io.limberapp.web.util.gs
import io.limberapp.web.util.xs
import kotlinx.css.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * A table showing form instances, and allowing them to be viewed by clicking on them.
 *
 * [formInstances] is the set of form instances to show on the table. One row for each.
 *
 * [formTemplates] is the form templates that [formInstances] are derived from. These are necessary in order to include
 * information that only exists on the [FormTemplateRep.Summary] and not on the [FormInstanceRep.Summary]. If this is
 * null, that data will be missing. However, it's recommended that [formTemplates] is only null if it's loading or if
 * there's an error, since the table will look incomplete without this information.
 */
internal fun RBuilder.formInstancesTable(
  formInstances: Set<FormInstanceRep.Summary>,
  formTemplates: Map<UUID, FormTemplateRep.Summary>?
) {
  child(component, Props(formInstances, formTemplates))
}

internal data class Props(
  val formInstances: Set<FormInstanceRep.Summary>,
  val formTemplates: Map<UUID, FormTemplateRep.Summary>?
) : RProps

private class S : Styles("FormInstancesTable") {
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

private val component = componentWithApi<Props> component@{ self, props ->
  self.loadUsers()

  if (props.formInstances.isEmpty()) {
    p { +"No forms exist." }
    return@component
  }

  limberTable(headers = listOf("#", "Created", null, "Type", "Creator")) {
    // TODO: Sort by unique sort key
    props.formInstances.forEach { formInstance ->
      limberTableRow(classes = s.c { it::row }) {
        attrs.key = formInstance.guid
        limberTableCell(classes = s.c { it::cell }) {
          val number = formInstance.number.toString()
          span(classes = gs.getClassName { it::visibleXs }) { small { +"#$number" } }
          span(classes = gs.getClassName { it::hiddenXs }) { +number }
        }
        limberTableCell(classes = s.c { it::cell }) {
          val createdDate = formInstance.createdDate.prettyRelative()
          span(classes = gs.getClassName { it::visibleXs }) { small { +createdDate } }
          span(classes = gs.getClassName { it::hiddenXs }) { +createdDate }
        }
        limberTableCell(hideContent = true, classes = s.c { it::cellBreak }) { }
        limberTableCell(classes = s.c { it::cell }) {
          props.formTemplates?.get(formInstance.formTemplateGuid)?.title?.let { +it }
        }
        limberTableCell(classes = s.c { it::cell }) {
          self.gs.users.stateOrNull?.get(formInstance.creatorAccountGuid)?.let {
            memberRow(it, small = true, hideNameXs = true)
          }
        }
      }
    }
  }
}
