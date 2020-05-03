package io.limberapp.backend.authorization.permissions

@Suppress("MagicNumber")
enum class OrgPermission(val bit: Int, val description: String) {
    MANAGE_ORG_ROLES(0, "Allows the user to manage the organization's roles."),
    MANAGE_ORG_ROLE_MEMBERSHIPS(1, "Allows the user to manage role memberships for all of the organization's members."),
    MANAGE_ORG_METADATA(2, "Allows the user to manage organization metadata such as the name and photo."),
    MANAGE_ORG_FEATURES(3, "Allows the user to create, manage, and delete organization features."),
}
