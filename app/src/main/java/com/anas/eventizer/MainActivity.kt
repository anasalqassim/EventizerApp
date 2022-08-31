package com.anas.eventizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.anas.eventizer.data.remote.EventLocationDto
import com.anas.eventizer.data.remote.PersonalEventDto
import com.anas.eventizer.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =  findNavController(R.id.fragmentContainerView)
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)

//
    }
}