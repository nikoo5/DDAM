package ar.edu.utn.frba.ddam.homie.helpers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import ar.edu.utn.frba.ddam.homie.R
import com.google.android.material.snackbar.Snackbar
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

        fun sendWhatsApp(view: View, phoneNumber: String, message: String) {
            val pm: PackageManager = view.context.packageManager
            val appInstalled: Boolean = try {
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }

            if (!appInstalled) {
                Snackbar.make(
                    view,
                    view.context.resources.getString(R.string.no_whatsapp),
                    Snackbar.LENGTH_SHORT
                ).show()
                return
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://api.whatsapp.com/send?phone=${phoneNumber}&text=${message}")
                view.context.startActivity(intent)
            }
        }
    }
}