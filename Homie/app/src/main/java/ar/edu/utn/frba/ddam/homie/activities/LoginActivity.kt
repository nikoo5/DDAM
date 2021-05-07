package ar.edu.utn.frba.ddam.homie.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.helpers.LocaleManager

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleManager.updateLocale(baseContext, PreferenceManager.getDefaultSharedPreferences(baseContext).getString("lang", "auto")!!);

        if (supportActionBar != null) supportActionBar?.hide();

        setContentView(R.layout.activity_login)
    }
}