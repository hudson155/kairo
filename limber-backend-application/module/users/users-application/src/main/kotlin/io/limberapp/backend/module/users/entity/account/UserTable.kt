package io.limberapp.backend.module.users.entity.account

import com.piperframework.store.SqlTable

internal object UserTable : SqlTable("users", "user") {

    val guid = uuid("guid")

    val identityProvider = bool("identity_provider")

    val superuser = bool("superuser")

    val name = text("name")

    val orgGuid = uuid("org_guid")

    val emailAddress = text("email_address")
    const val emailAddressUniqueConstraint = "user_email_address_key"

    val firstName = text("first_name")

    val lastName = text("last_name")

    val profilePhotoUrl = text("profile_photo_url").nullable()
}
