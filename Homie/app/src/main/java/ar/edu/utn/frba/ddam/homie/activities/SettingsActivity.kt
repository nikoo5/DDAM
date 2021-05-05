package ar.edu.utn.frba.ddam.homie.activities


import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.helpers.LocaleManager
import ar.edu.utn.frba.ddam.homie.helpers.Utils

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleManager.updateLocale(baseContext, "en");
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.flSettings, SettingsFragment())
                .commit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setTitle(R.string.settings);

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.xml_settings_main, rootKey)
        }
    }

    override fun onDestroy() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }



    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "lang" -> {
                val lang = sharedPreferences.getString(key, "")!!;
                LocaleManager.updateLocale(baseContext, lang)
            }
            "theme" -> Utils.updateDarkMode(baseContext)
        }
    }
}