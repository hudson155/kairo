@file:JsModule("jsonwebtoken")
@file:JsNonModule

package io.limberapp.web.util.external

@JsName("decode")
internal external fun decodeJwt(jwt: String): dynamic
