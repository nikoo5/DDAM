package ar.edu.utn.frba.ddam.homie.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.utn.frba.ddam.homie.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null) supportActionBar?.hide();

        setContentView(R.layout.activity_login)
    }
}