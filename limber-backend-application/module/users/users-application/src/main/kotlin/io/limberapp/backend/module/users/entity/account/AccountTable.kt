package io.limberapp.backend.module.users.entity.account

import com.piperframework.store.SqlTable

internal object AccountTable : SqlTable("users", "account") {

    val guid = uuid("guid")

    val identityProvider = bool("identity_provider")

    val superuser = bool("superuser")

    val name = text("name")
}
