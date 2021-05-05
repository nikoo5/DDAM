package ar.edu.utn.frba.ddam.homie.helpers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import java.security.MessageDigest
import java.util.*

class Utils {
    companion object {
        fun getString(context: Context, name: String) : String {
            val res: Resources = context.resources
            return res.getString(res.getIdentifier(name, "string", context.packageName))
        }

        fun generateHash(length: Int = 32): String {
            val len = length.coerceIn(12, 32)
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..len)
                    .map { allowedChars.random() }
                    .joinToString("")
        }

        fun updateDarkMode(context: Context) {
            when (PreferenceManager.getDefaultSharedPreferences(context).getString("theme", "auto")) {
                "auto" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}