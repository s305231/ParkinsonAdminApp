package com.example.adminparkinson.Activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.adminparkinson.R
import com.example.adminparkinson.database.FireStoreClass
import com.example.adminparkinson.model.Workout
import com.example.adminparkinson.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_workout.*
import java.io.IOException

class CreateWorkoutActivity : AuxActivity() {

    private var mSelectedImageFileUri : Uri? = null
    private lateinit var mUserName: String
    private var mWorkoutImageFileURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)
        setupActionBar()

        btn_create.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadWorkoutImage()
            }else {
                showProgressDialog(resources.getString(R.string.please_wait))
                createWorkout()
            }
        }

        //We do it this way beacuse we get the username without doin another db request. This done in MainActivty in fab onclicklistener
        if (intent.hasExtra(Constants.NAME)) {
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        iv_workout_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERIMISSION_CODE
                )
            }
        }
    }

    //Creating board
    private fun createWorkout() {
        val asssignedUsersArrayList: ArrayList<String> = ArrayList()
        asssignedUsersArrayList.add(getCurrentUserID())
        //New worout object from Workout Model
        var workout = Workout(et_workout_name.text.toString(), mWorkoutImageFileURL, mUserName, asssignedUsersArrayList)

        //The actual boards are created in FireStore class, and not here. Because we
        //want everything that has to with db be in that class
        FireStoreClass().createWorkout(this,workout)
    }

    //Function to upload to the cloud storage, this one is for images
    private fun uploadWorkoutImage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFileUri != null) {
            //This done to give image a unique name. For ex. It will be called user_image + the time + the fileExtension at the end
            val sRef : StorageReference = FirebaseStorage.getInstance().reference.child("WORKOUT_IMAGE" + System.currentTimeMillis()
                    + "." + getFileExtenstion(this, mSelectedImageFileUri))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                    taskSnapshot ->
                Log.e("Workout Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl!!.toString())

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                    Log.i("Downloadable Image Url", uri.toString())
                    mWorkoutImageFileURL = uri.toString()
                    createWorkout()
                }.addOnFailureListener {
                        exception ->
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                    hideProgressDialog()
                }
            }
        } else println("Please choose a image for the exercise")
    }

    fun workoutCreatedSuccessfully() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }



    private fun setupActionBar() {
        setSupportActionBar(toolbar_create_workout_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = resources.getString(R.string.create_workout_title)
        }
        toolbar_create_workout_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERIMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                showImageChooser(this)

            } else {
                Toast.makeText(this, "Oops, you just denied the permission for storage. You can also allow it for settings", Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data        }

        try {
            Glide
                .with(this)
                .load(mSelectedImageFileUri)
                .centerCrop()
                .placeholder(R.drawable.ic_workout_place_holder)
                .into(iv_workout_image)
        }catch (e: IOException) {
            e.printStackTrace()
        }
    }
}