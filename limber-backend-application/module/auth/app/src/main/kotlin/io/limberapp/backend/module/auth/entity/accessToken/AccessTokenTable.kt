package io.limberapp.backend.module.auth.entity.accessToken

import com.piperframework.store.SqlTable

object AccessTokenTable : SqlTable("auth", "access_token") {

    val guid = uuid("guid")

    val accountGuid = uuid("account_guid")

    val token = text("token")
}
