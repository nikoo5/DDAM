package ar.edu.utn.frba.ddam.homie.helpers

import android.content.Context
import android.content.res.Resources
import java.security.MessageDigest

class Utils {
    companion object {
        fun getString(context: Context, name: String) : String {
            val res: Resources = context.resources
            return res.getString(res.getIdentifier(name, "string", context.packageName))
        }

        fun generateHash(length : Int = 32): String {
            var len : Int = 32;
            if (length >= 1 && length <= 32) len = length;
            val bytes = this.toString().toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) }).substring(32 - len)
        }
    }
}