package io.limberapp.common.util.darb

@JsExport
fun decodeDarb(darb: String): Array<Boolean> = DarbEncoder.decode(darb).toTypedArray()
