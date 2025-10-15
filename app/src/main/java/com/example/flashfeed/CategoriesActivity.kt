package com.example.flashfeed

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flashfeed.databinding.ActivityCategoriesBinding
import com.example.flashfeed.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoriesActivity : AppCompatActivity() {
    private lateinit var b:ActivityCategoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(b.root)
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
        setTitle("Categories")
        b.general.setOnClickListener {
            val i =Intent(this,MainActivity::class.java)
            i.putExtra("category","general")
            startActivity(i)
        }
        b.health.setOnClickListener { val i =Intent(this,MainActivity::class.java)
            i.putExtra("category","health")
            startActivity(i) }
        b.tech.setOnClickListener { val i =Intent(this,MainActivity::class.java)
            i.putExtra("category","technology")
            startActivity(i) }
        b.entertainment.setOnClickListener { val i =Intent(this,MainActivity::class.java)
            i.putExtra("category","entertainment")
            startActivity(i) }
        b.science.setOnClickListener {val i =Intent(this,MainActivity::class.java)
            i.putExtra("category","science")
            startActivity(i)  }
        b.sport.setOnClickListener { val i =Intent(this,MainActivity::class.java)
            i.putExtra("category","sports")
            startActivity(i) }
        b.Business.setOnClickListener { val i =Intent(this,MainActivity::class.java)
            i.putExtra("category","business")
            startActivity(i) }





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater =menuInflater
        inflater.inflate(R.menu.menu,menu)


        return true
    }
}


