package ar.edu.utn.frba.ddam.homie.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.entities.User
import ar.edu.utn.frba.ddam.homie.helpers.LocaleManager
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.bumptech.glide.util.Util
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var dlMain : DrawerLayout
    private lateinit var bnvMenu : BottomNavigationView
    private lateinit var nvMenu : NavigationView

    private lateinit var nhfMain : NavHostFragment
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleManager.updateLocale(baseContext, PreferenceManager.getDefaultSharedPreferences(baseContext).getString("lang", "auto")!!);

        setContentView(R.layout.activity_main)
        dlMain = findViewById(R.id.dlMain);
        bnvMenu = findViewById(R.id.bnvMenu);
        nvMenu = findViewById(R.id.nvMenu);

        nhfMain = supportFragmentManager.findFragmentById(R.id.nhfMain) as NavHostFragment;
        NavigationUI.setupWithNavController(bnvMenu, nhfMain.navController)

        navController = Navigation.findNavController(this, R.id.nhfMain);
        nvMenu.setupWithNavController(navController);
        NavigationUI.setupActionBarWithNavController(this, navController, dlMain);
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, dlMain);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_left, menu);
        return false;
    }

    override fun onBackPressed() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    fun logOutHandler(menuItem : MenuItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.logout));
        builder.setMessage(resources.getString(R.string.logout_question))

        builder.setPositiveButton(resources.getString(R.string.yes).toUpperCase(), DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(this, LoginActivity::class.java));
            finish();
        })

        builder.setNegativeButton(resources.getString(R.string.no).toUpperCase(), DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss();
        })

        builder.create().show();
    }
}