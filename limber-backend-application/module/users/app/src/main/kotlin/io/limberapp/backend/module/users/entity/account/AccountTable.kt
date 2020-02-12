package io.limberapp.backend.module.users.entity.account

import com.piperframework.store.SqlTable

object AccountTable : SqlTable("users", "account") {

    val guid = uuid("guid")

    val name = text("name")

    val identityProvider = bool("identity_provider")

    val superuser = bool("superuser")
}
