package io.limberapp.backend.module.users.entity.user

import com.piperframework.store.SqlTable

object UserTable : SqlTable("users", "user") {
    val accountGuid = uuid("account_guid").uniqueIndex().references(AccountTable.guid)
    val emailAddress = text("email_address").uniqueIndex()
    val firstName = text("first_name")
    val lastName = text("last_name")
    val profilePhotoUrl = text("profile_photo_url").nullable()
}
