package com.devandreschavez.tumascota

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.devandreschavez.tumascota.core.hide
import com.devandreschavez.tumascota.core.show
import com.devandreschavez.tumascota.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.loginFragment -> {
                    binding.bottomNavigationView.hide()
                    binding.AppBar.hide()
                }
                R.id.registerFragment -> {
                    binding.bottomNavigationView.hide()
                    binding.AppBar.hide()
                }
                R.id.userReportsFragment -> {
                    binding.bottomNavigationView.hide()
                    binding.AppBar.hide()
                }
                else -> {
                    binding.bottomNavigationView.show()
                    binding.AppBar.show()
                }
            }
        }
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.exitOption -> {
                    FirebaseAuth.getInstance().signOut()
                    navController.clearBackStack(R.id.loginFragment)
                    true
                }
                R.id.reports -> {
                    navController.navigate(R.id.userReportsFragment)
                    true
                }
                else -> false
            }
        }
    }
}