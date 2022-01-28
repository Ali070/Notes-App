package com.alisamir.notesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.alisamir.notesapp.R
import com.alisamir.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var controller: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        controller = navFragment.navController
        setupActionBarWithNavController(controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        return controller.navigateUp()||super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}