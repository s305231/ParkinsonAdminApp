package com.example.adminparkinson.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminparkinson.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AuxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llExercises.setOnClickListener {

            // val intent = Intent(this, WorkoutListActivity::class.java)
            val intent = Intent(this, ViewAllExercises::class.java)
            startActivity(intent)
        }
        llUsers.setOnClickListener {
            val intent = Intent(this, ViewAllUsers::class.java)
            startActivity(intent)
        }

        llWorkouts.setOnClickListener {
            val intent = Intent(this, WorkoutListActivity::class.java)
            startActivity(intent)
        }



    }


}