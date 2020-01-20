package io.limberapp.backend.module.users.entity.account

import com.piperframework.store.SqlTable

object UserTable : SqlTable("users", "user") {
    val accountGuid = uuid("account_guid").references(AccountTable.guid)
    val emailAddress = text("email_address")
    val firstName = text("first_name")
    val lastName = text("last_name")
    val profilePhotoUrl = text("profile_photo_url").nullable()
}
