package com.example.flashfeed


import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flashfeed.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    val prefs_name = "AppSettingsPrefs"
    val country_key = "preferred_country"

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Add the countries you want
        val countries = listOf(
            "United States",
            "United Kingdom",
            "Egypt"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            countries
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = adapter

        val prefs = getSharedPreferences(prefs_name, Context.MODE_PRIVATE)

        binding.btnSave.setOnClickListener {
            val selectedCountry = binding.spinnerCountry.selectedItem.toString()
            prefs.edit().putString(country_key, selectedCountry).apply()
            Toast.makeText(this, "Country saved: $selectedCountry", Toast.LENGTH_SHORT).show()
            //optional
            finish()
        }
    }
}