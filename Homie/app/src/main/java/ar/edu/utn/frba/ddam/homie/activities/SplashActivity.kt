package ar.edu.utn.frba.ddam.homie.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.helpers.LocaleManager
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private var SPLAH_TIME : Long = 3000;

    lateinit var mAuth: FirebaseAuth

    lateinit var topAnim : Animation
    lateinit var bottomAnim : Animation

    lateinit var ivSplashCity : ImageView
    lateinit var tvSplashText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleManager.updateLocale(baseContext, "en");
        mAuth = FirebaseAuth.getInstance();

        if (supportActionBar != null) supportActionBar?.hide();

        setContentView(R.layout.activity_splash)

        Utils.updateDarkMode(baseContext);

        val currentUser = mAuth.currentUser;
        if(currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java));
            finish();
        } else {

            topAnim = AnimationUtils.loadAnimation(this, R.anim.anim_top);
            bottomAnim = AnimationUtils.loadAnimation(this, R.anim.anim_bottom);
            ivSplashCity = findViewById(R.id.ivSplashCity);
            tvSplashText = findViewById(R.id.tvSplashText);

            tvSplashText.startAnimation(topAnim);
            ivSplashCity.startAnimation(bottomAnim);

            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java));
                finish();
            }, SPLAH_TIME)
        }
    }
}