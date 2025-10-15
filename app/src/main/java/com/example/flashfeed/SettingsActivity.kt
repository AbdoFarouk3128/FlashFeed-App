package com.example.flashfeed


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flashfeed.databinding.ActivitySettingsBinding
//lateinit var prefs:SharedPreferences
class SettingsActivity : AppCompatActivity() {

    val prefs_name = "AppSettingsPrefs"
    val country_key = "preferred_country"

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()// and this too(Abdelrahman latif)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //I(abdelrahman latif) Added this
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(
                view.paddingLeft,
                statusBarHeight,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
        setTitle("Settings")
        //End of Abdelrahman Latif
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
//I (abdelrahman latif) removed  val bec. i declared it to all classes
        val  prefs = getSharedPreferences(prefs_name, Context.MODE_PRIVATE)
        val country =prefs.getString(country_key,"NO data")
        binding.btnSave.setOnClickListener {
            val selectedCountry = binding.spinnerCountry.selectedItem.toString()
            prefs.edit().putString(country_key, selectedCountry).apply()
            Toast.makeText(this, "Country saved: $selectedCountry", Toast.LENGTH_SHORT).show()
            //optional
            finish()
        }
    }
}