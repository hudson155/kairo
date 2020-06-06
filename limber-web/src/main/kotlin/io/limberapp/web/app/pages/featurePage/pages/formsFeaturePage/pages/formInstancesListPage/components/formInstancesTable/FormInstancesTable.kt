package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.components.formInstancesTable

import com.piperframework.types.UUID
import com.piperframework.util.prettyRelative
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
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

private val component = component<Props> component@{ props ->
  if (props.formInstances.isEmpty()) {
    p { +"No forms exist." }
    return@component
  }

  limberTable(headers = listOf("#", "Created", "Type")) {
    // TODO: Sort by unique sort key
    props.formInstances.forEach { formInstance ->
      limberTableRow {
        attrs.key = formInstance.guid
        td { +formInstance.number.toString() }
        td { +formInstance.createdDate.prettyRelative() }
        td { props.formTemplates?.get(formInstance.formTemplateGuid)?.title?.let { +it } }
      }
    }
  }
}
