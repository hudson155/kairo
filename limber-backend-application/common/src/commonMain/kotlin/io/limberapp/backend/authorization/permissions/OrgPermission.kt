package io.limberapp.backend.authorization.permissions

enum class OrgPermission(val bit: Int, val description: String) {
    MANAGE_ORG_PERMISSIONS(0, "Allows the user to manage permissions for all users within the organization."),
    MANAGE_ORG_METADATA(1, "Allows the user to manage organization metadata such as the name and photo."),
    MANAGE_ORG_FEATURES(2, "Allows the user to create, manage, and delete organization features."),
}
