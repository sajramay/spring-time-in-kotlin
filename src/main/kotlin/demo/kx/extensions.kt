package demo.kx

import java.util.*

fun applyAction(vararg s: String, action: (String) -> Unit) {
    s.forEach(action)
}
fun String.uuid() : String = UUID.nameUUIDFromBytes(this.encodeToByteArray()).toString()