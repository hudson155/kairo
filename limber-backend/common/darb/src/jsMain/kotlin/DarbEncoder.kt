import io.limberapp.util.darb.DarbEncoder

@JsExport
fun decodeDarb(darb: String): Array<Boolean> = DarbEncoder.decode(darb).toTypedArray()
