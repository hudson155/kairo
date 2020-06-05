package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.components.formInstancesTable

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.app.components.limberTable.components.limberTableRow.limberTableRow
import io.limberapp.web.app.components.limberTable.limberTable
import io.limberapp.web.util.component
import react.*
import react.dom.*

/**
 * A table showing form instances, and allowing them to be viewed by clicking on them.
 *
 * [formInstances] is the set of roles to show on the table. One row for each.
 */
internal fun RBuilder.formInstancesTable(formInstances: Set<FormInstanceRep.Summary>) {
  child(component, Props(formInstances))
}

internal data class Props(val formInstances: Set<FormInstanceRep.Summary>) : RProps

private val component = component<Props> component@{ props ->
  if (props.formInstances.isEmpty()) {
    p { +"No forms exist." }
    return@component
  }

  limberTable(headers = listOf("#", "guid")) {
    // TODO: Sort by unique sort key
    props.formInstances.forEach { formInstance ->
      limberTableRow {
        attrs.key = formInstance.guid
        td { +formInstance.number.toString() }
        td { +formInstance.guid }
      }
    }
  }
}
