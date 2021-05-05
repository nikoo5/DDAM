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
            var len : Int = 32;
            if (length >= 1 && length <= 32) len = length;
            val bytes = this.toString().toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) }).substring(32 - len)
        }

        fun updateLocales(context: Context) {
            val lang = PreferenceManager.getDefaultSharedPreferences(context).getString("lang", "")!!
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            context.resources.configuration.updateFrom(config)
//            context.resources.updateConfiguration(
//                config,
//                context.resources.displayMetrics
//            )
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