package io.limberapp.backend.module.auth.entity.personalAccessToken

import com.piperframework.store.SqlTable

object PersonalAccessTokenTable : SqlTable("auth", "access_token") {
    val guid = uuid("guid")
    val accountGuid = uuid("account_guid")
    val token = text("token")
}
