package io.limberapp.backend.module.users.entity.user

import com.piperframework.store.SqlTable

object UserTable : SqlTable("users", "user") {
    val accountId = long("account_id").uniqueIndex().references(AccountTable.id)
    val emailAddress = text("email_address").uniqueIndex()
    val firstName = text("first_name")
    val lastName = text("last_name")
    val profilePhotoUrl = text("profile_photo_url").nullable()
}
