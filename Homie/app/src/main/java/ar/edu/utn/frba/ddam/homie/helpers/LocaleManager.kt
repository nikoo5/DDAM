package ar.edu.utn.frba.ddam.homie.helpers

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.core.os.persistableBundleOf
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.consumesAll
import java.util.*

class LocaleManager {
    constructor()

    companion object {

        fun updateLocale(context: Context, language : String) : Context {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return updateResources(context, language);
            }
            return updateResourcesLegacy(context, language);
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun updateResources(context: Context, language: String) : Context {
            val locale = Locale("en");
            Locale.setDefault(locale)

            val config = context.resources.configuration
            config.setLocale(locale);
            config.setLayoutDirection(locale);

            return context;
        }

        @SuppressWarnings("deprecation")
        fun updateResourcesLegacy(context: Context, language: String) : Context {
            val locale = Locale(language);
            Locale.setDefault(locale)

            val res = context.resources
            val config = res.configuration
            config.locale = locale;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLayoutDirection(locale);
            }
            res.updateConfiguration(config, res.displayMetrics);

            return context;
        }
    }
}