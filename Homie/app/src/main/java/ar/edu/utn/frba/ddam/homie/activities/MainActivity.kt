package ar.edu.utn.frba.ddam.homie.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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
}