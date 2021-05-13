package com.example.adminparkinson.Activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.adminparkinson.R
import com.example.adminparkinson.database.FireStoreClass
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val typeFace: Typeface = Typeface.createFromAsset(assets, "Android7-zO6X.ttf")
        tv_app_name.typeface = typeFace

        Handler().postDelayed({

            var currentUserID = FireStoreClass().getCurrentUserId()
            if (currentUserID.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            }else {
                startActivity(Intent(this, EntryActivity::class.java))
            }
            finish()
        }, 2500)
    }
}