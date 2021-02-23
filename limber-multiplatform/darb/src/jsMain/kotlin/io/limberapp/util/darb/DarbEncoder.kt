package io.limberapp.util.darb

@JsExport
fun decodeDarb(darb: String): Array<Boolean> = DarbEncoder.decode(darb).toTypedArray()
