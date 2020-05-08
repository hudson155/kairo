package io.limberapp.web.util

import com.piperframework.util.unknownValue
import kotlin.js.Date
import kotlin.math.abs

@Suppress("StringLiteralDuplication")
internal object Strings {
    const val clickHereToSignIn = "Click here to sign in"
    val copyright = "Copyright © ${Date().getFullYear()} ${process.env.COPYRIGHT_HOLDER}"
    const val formsTitle = "Forms"
    const val homepageDescription = "This is the homepage."
    const val homepageTitle = "Home"
    const val identifyingYou = "Identifying you..."
    const val limber = "Limber"
    const val loadingOrg = "Loading org..."
    const val loadingUser = "Loading user..."
    const val members = "members"
    const val noRolesAreDefined = "No roles are defined."
    const val notFoundTitle = "Not Found"
    const val orgRoleNameTitle = "Name"
    const val orgRolePermissions = "permissions"
    const val orgRolePermissionsTitle = "Permissions"
    const val orgRoleMembersTitle = "Members"
    const val orgSettings = "Organization settings"
    const val orgSettingsRolesPageDescription = "Roles grant users permissions within your organization."
    const val pageNotFoundDescription = "We looked everywhere, but we couldn't find the page you were looking for."
    const val signIn = "Sign in"
    const val signOut = "Sign out"
    const val signedInAs = "Signed in as"
    const val welcomeTitle = "Welcome to Limber"
}

internal fun String.pluralize(count: Int) = when (this) {
    "members" -> pluralize("member", "members", count)
    "permissions" -> pluralize("permission", "permissions", count)
    else -> unknownValue("pluralization target", this)
}

private fun pluralize(singular: String, plural: String, count: Int): String {
    if (abs(count) == 1) return singular
    return plural
}
