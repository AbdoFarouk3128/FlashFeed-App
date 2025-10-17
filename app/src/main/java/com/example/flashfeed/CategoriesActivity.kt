package com.example.flashfeed

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flashfeed.databinding.ActivityCategoriesBinding
import com.example.flashfeed.databinding.ActivitySettingsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class CategoriesActivity : AppCompatActivity() {
    private lateinit var b: ActivityCategoriesBinding
    private lateinit var bs: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityCategoriesBinding.inflate(layoutInflater)
        bs = ActivitySettingsBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_SCALED);
        }
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(
                view.paddingLeft,
                statusBarHeight,
                view.paddingRight,
                view.paddingBottom,
            )
            insets
        }
        setupAnimations()

        setTitle("Categories")

        b.general.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("category", "general")
            startActivity(i)
        }
        b.health.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("category", "health")
            startActivity(i)
        }
        b.tech.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("category", "technology")
            startActivity(i)
        }
        b.entertainment.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("category", "entertainment")
            startActivity(i)
        }
        b.science.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("category", "science")
            startActivity(i)
        }
        b.sport.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("category", "sports")
            startActivity(i)
        }
        b.Business.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("category", "business")
            startActivity(i)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val i = Intent(this, SettingsActivity::class.java)
            startActivity(i)
        } else if (item.itemId == R.id.log_out) {

            //sign out(kero)

            Firebase.auth.signOut()
            val i = Intent(this, Login::class.java)
            startActivity(i)
            finish()

        }
        return super.onOptionsItemSelected(item)
    }
    private fun setupAnimations() {
        val animSlideInRight =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        val animSlideInLeft =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        val animSlideInTop =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
      val fadeIn=
          AnimationUtils.loadAnimation(this,R.anim.fade_in)

        b.general.startAnimation(animSlideInTop)
        b.Business.startAnimation(animSlideInLeft)
        b.entertainment.startAnimation(animSlideInRight)
        b.health.startAnimation(animSlideInLeft)
        b.sport.startAnimation(animSlideInLeft)
        b.science.startAnimation(animSlideInRight)
        b.tech.startAnimation(animSlideInRight)


        b.generalTv.startAnimation(fadeIn)
        b.techTv.startAnimation(fadeIn)
        b.businessTv.startAnimation(fadeIn)
        b.sportTv.startAnimation(fadeIn)
        b.entertainmentTv.startAnimation(fadeIn)
        b.scienceTv.startAnimation(fadeIn)
        b.healthTv.startAnimation(fadeIn)
    }
}


