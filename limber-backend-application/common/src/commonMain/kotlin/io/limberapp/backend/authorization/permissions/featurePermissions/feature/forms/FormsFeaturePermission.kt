package io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermission

@Suppress("MagicNumber")
enum class FormsFeaturePermission(
  override val bit: Int,
  override val title: String,
  override val description: String,
) : FeaturePermission {
  MANAGE_FORM_TEMPLATES(
    bit = 0,
    title = "Manage form templates",
    description = "Allows the user to create, manage, and delete form templates."
  ),
  CREATE_FORM_INSTANCES(
    bit = 1,
    title = "Create form instances",
    description = "Allows the user to fill out forms."
  ),
  MODIFY_OWN_FORM_INSTANCES(
    bit = 2,
    title = "Modify own form instances",
    description = "Allows the user to modify responses to forms that they've submitted."
  ),
  DELETE_OWN_FORM_INSTANCES(
    bit = 3,
    title = "Delete own form instances",
    description = "Allows the user to delete responses to forms that they've submitted."
  ),
  SEE_OTHERS_FORM_INSTANCES(
    bit = 4,
    title = "See others' form instances",
    description = "Allows the user to see other users' responses to forms."
  ),
  MODIFY_OTHERS_FORM_INSTANCES(
    bit = 5,
    title = "Modify others' form instances",
    description = "Allows the user to modify responses to forms that other users have submitted."
  ),
  DELETE_OTHERS_FORM_INSTANCES(
    bit = 6,
    title = "Delete others' form instances",
    description = "Allows the user to delete responses to forms that other users have submitted."
  ),
  EXPORT_FORM_INSTANCES(
    bit = 7,
    title = "Export form instances",
    description = "Allows the user to export form responses in bulk."
  ),
}
