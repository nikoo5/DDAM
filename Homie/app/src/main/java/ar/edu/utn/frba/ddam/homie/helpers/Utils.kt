package ar.edu.utn.frba.ddam.homie.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import ar.edu.utn.frba.ddam.homie.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class Utils {
    companion object {
        private val PREF_TAG = "USER_PREFERENCES"

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

        fun setImage(context : Context, view : View?, imageView : ImageView, progressBar: ProgressBar?, imageUrl : String, imgDefault : Int?, errorMessage : String, onReady : (Boolean) -> Unit = {_ ->}) {
            if(progressBar != null) progressBar.visibility = View.VISIBLE
            if (imageUrl != "") {
                Glide.with(context)
                    .load(imageUrl)
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            if(imgDefault != null) imageView.setImageResource(imgDefault)
                            if(progressBar != null) progressBar.visibility = View.GONE
                            if(view != null && errorMessage != "") Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show()
                            onReady(false);
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if(progressBar != null) progressBar.visibility = View.GONE
                            onReady(true);
                            return false
                        }
                    })
                    .into(imageView)
            } else {
                if(imgDefault != null) imageView.setImageResource(imgDefault)
                if(progressBar != null) progressBar.visibility = View.GONE
            }
        }

        fun getLastUpdate(context: Context) : Date {
            val sharedPreferences = context.getSharedPreferences(PREF_TAG, Context.MODE_PRIVATE)
            var date = Date(LocalDate.parse("01/01/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")).toEpochDay())
            var time = sharedPreferences.getLong("LAST_UPDATE", 0.toLong())

            if(time == 0.toLong()) {
                time = date.time
                setLastUpdate(context, date)
            } else {
                return Date(time)
            }

            return date
        }

        fun setLastUpdate(context: Context, date : Date) {
            val sharedPreferences = context.getSharedPreferences(PREF_TAG, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putLong("LAST_UPDATE", date.time)
            editor.apply()
        }

        fun clearPref(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREF_TAG, Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
        }
    }
}