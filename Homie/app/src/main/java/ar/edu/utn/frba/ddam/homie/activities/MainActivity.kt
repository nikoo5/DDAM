package ar.edu.utn.frba.ddam.homie.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ar.edu.utn.frba.ddam.homie.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var dlMain : DrawerLayout
    private lateinit var bnvMenu : BottomNavigationView
    private lateinit var nvMenu : NavigationView
    private lateinit var nhfMain : NavHostFragment
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnvMenu = findViewById(R.id.bnvMenu);
        nhfMain = supportFragmentManager.findFragmentById(R.id.nhfMain) as NavHostFragment;
        NavigationUI.setupWithNavController(bnvMenu, nhfMain.navController)

        dlMain = findViewById(R.id.dlMain);
        navController = Navigation.findNavController(this, R.id.nhfMain);
        nvMenu = findViewById(R.id.nvMenu);
        nvMenu.setupWithNavController(navController);
        NavigationUI.setupActionBarWithNavController(this, navController, dlMain);
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, dlMain);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_left, menu);
        return true;
    }

    override fun onBackPressed() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    fun settingHandler(menuItem : MenuItem) {
        Snackbar.make(dlMain, resources.getString(R.string.future_feature), Snackbar.LENGTH_SHORT).show();
    }

    fun logOutHandler(menuItem : MenuItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.logout));
        builder.setMessage(resources.getString(R.string.logout_question))

        builder.setPositiveButton(resources.getString(R.string.yes).toUpperCase(), DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(this, LoginActivity::class.java));
            finish();
        })

        builder.setNegativeButton(resources.getString(R.string.no).toUpperCase(), DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss();
        })

        builder.create().show();
    }
}