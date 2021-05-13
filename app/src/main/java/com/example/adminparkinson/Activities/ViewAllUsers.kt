package com.example.adminparkinson.Activities

import android.os.Bundle
import androidx.appcompat.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminparkinson.R
import com.example.adminparkinson.adapter.AllUserAdapter
import com.example.adminparkinson.adapter.AllUserAdapterSS
import com.example.adminparkinson.database.FireStoreClass
import com.example.adminparkinson.model.User
import com.example.adminparkinson.model.Workout
import com.example.adminparkinson.utils.Constants
import kotlinx.android.synthetic.main.activity_view_all_users.*
import kotlinx.android.synthetic.main.item_all_users.*
import kotlinx.android.synthetic.main.item_all_users.view.*


class ViewAllUsers : AuxActivity() {

    private lateinit var mWorkoutDetails: Workout
    var workoutDocumentId = ""
    private var mActionMode: ActionMode? = null
    private var activateMutiSelect: Boolean = false
    var selectedUsersArrayList: ArrayList<String> = ArrayList()
    private var assignedSuccessfully : Boolean = false

    val selectUsersArrayList: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_users)
        setupActionBar()
        FireStoreClass().getUserList(this)


        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            workoutDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
            println("Intent has extra doc id: " + workoutDocumentId)
            //Inside beacuse if we get here from mainactivity theres no doc id -> error
            FireStoreClass().getBoardDetails(this, workoutDocumentId)
        }








        fab_create_user.setOnClickListener {
            //We do it this way beacuse we get the username without doin another db request
            //val intent = Intent(this, SignUpActivity::class.java)
            //startActivity(intent)

            /*if (intent.hasExtra("WORKOUT")) {
                var wah = intent.extras?.getParcelable<Workout>("WORKOUT")!!
                if (wah != null) {
                    mWorkoutDetails = wah
                }
            }*/

            println("From On create workout is: " + mWorkoutDetails.name + " selectedUsersArrayList: " + selectUsersArrayList)

            for (userId in selectedUsersArrayList) {

                mWorkoutDetails.assignedTo.add(0, userId)
                println("from FAB: " + userId)
            }

            FireStoreClass().updateWorkout(this@ViewAllUsers, mWorkoutDetails)

        }





    }

    fun workoutDetails(workout: Workout) {

        mWorkoutDetails = workout
    }

    //The downloading of the workouts(boards) will happen else where (getWorkoutList in Firestore class), this function will display the workouts ot UI
    fun populateUserList(userList: ArrayList<User>) {


        hideProgressDialog()
        if (userList.size > 0) {
            rv_all_user_list.visibility = View.VISIBLE
            no_users_available.visibility = View.GONE

            rv_all_user_list.layoutManager = LinearLayoutManager(this)
            rv_all_user_list.setHasFixedSize(true)

            val adapter = AllUserAdapter(this, userList)
            rv_all_user_list.adapter = adapter

            adapter.setOnClickListener(object : AllUserAdapter.OnClickListener {
                override fun onClick(position: Int, model: User) {

                    if (intent.hasExtra("WORKOUT")) {
                        var wah = intent.extras?.getParcelable<Workout>("WORKOUT")!!
                        if (wah != null) {

                            mWorkoutDetails = wah

                            mWorkoutDetails.assignedTo.add(0, model.id)
                            showProgressDialog(resources.getString(R.string.please_wait))
                            FireStoreClass().updateWorkout(this@ViewAllUsers, mWorkoutDetails)
                            assignedSuccessfully = true

                        }

                        if (assignedSuccessfully) {
                            Toast.makeText(this@ViewAllUsers, mWorkoutDetails.name + " successfully assigned to " + model.name, Toast.LENGTH_SHORT).show()
                            println("Assigned succesfully to: " + model.name)
                            onBackPressed()
                            assignedSuccessfully = false

                        } else {
                            Toast.makeText(this@ViewAllUsers, "Error while assigning user", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
            })


        }
    }


            /*adapter.setOnLongItemClickListener(object : AllUserAdapter.OnLongItemClick {
                override fun onLongPress(position: Int, model: User) {
                    println("Long press pressed G, postion is: " + position)
                }


            })*/

            //-------MultiSelect starts here ---\\


           /* adapter.setOnItemClickListener(
                object : AllUserAdapterSS.OnItemClick {


                    /*override fun onClick(position: Int, model: User) {
                    println("Something will happen when an user is pressed, however position is: " + position + " and model is: " + model)

                    if (intent.hasExtra("WORKOUT")) {

                        var wah = intent.extras?.getParcelable<Workout>("WORKOUT")!!
                        if (wah != null) {

                            mWorkoutDetails = wah

                            mWorkoutDetails.assignedTo.add(0, model.id)
                            showProgressDialog(resources.getString(R.string.please_wait))
                            FireStoreClass().updateWorkout(this@ViewAllUsers, mWorkoutDetails)
                            assignedSuccessfully = true

                        }

                        if (assignedSuccessfully) {

                            Toast.makeText(this@ViewAllUsers, mWorkoutDetails.name + " successfully assigned to " + model.name, Toast.LENGTH_SHORT).show()
                            onBackPressed()
                            assignedSuccessfully = false

                        }



                    }



                }*/

                    override fun onItemClick(position: Int, model: User) {

                        println("Something will happen when an user is pressed, however position is: " + position + " and model is: " + model)

                      /*  println("TESTY pos: " + position)
                        println("Size of the thing is " + selectUsersArrayList.size + " USer is: " + selectUsersArrayList)

                        if (activateMutiSelect) {
                            println("Selected array list contains: " + selectedUsersArrayList.size + " number of users and these users: " + selectedUsersArrayList)
                        }*/


                    }


                    override fun onLongPress(position: Int, model: User) {

                    }


                })

        } else {
            println("User List size less than 0: " + userList.size)
            rv_all_user_list.visibility = View.GONE
            no_users_available.visibility = View.VISIBLE
        }*/





    fun addUpdateWorkoutSuccess() {
        hideProgressDialog()
        println("Workout assigned user list updated successfully?")

    }

    fun createWorkout(selectedUserList: ArrayList<String>) {

        //showProgressDialog(resources.getString(R.string.please_wait))

        for (userId in selectedUserList) {
            println("userids: " + userId)
            //selectedUsersArrayList = selectedUserList
        }

        //FireStoreClass().updateWorkout(this@ViewAllUsers, mWorkoutDetails)
        assignedSuccessfully = true


        if (assignedSuccessfully) {

            //Toast.makeText(this@ViewAllUsers, mWorkoutDetails.name + " successfully assigned ", Toast.LENGTH_SHORT).show()
            //onBackPressed()
            assignedSuccessfully = false

            }

        }



    private fun createWorkout_() {

       /* if (intent.hasExtra("WORKOUT")) {

            var wah = intent.extras?.getParcelable<Workout>("WORKOUT")!!
            if (wah != null) {

                mWorkoutDetails = wah

            }

            val asssignedUsersArrayList: ArrayList<String> = ArrayList()
            asssignedUsersArrayList.add(getCurrentUserID())
            //New worout object from Workout Model
            var workout = Workout(
                et_workout_name.text.toString(),
                mWorkoutImageFileURL,
                mUserName,
                asssignedUsersArrayList
            )

            //The actual boards are created in FireStore class, and not here. Because we
            //want everything that has to with db be in that class
            FireStoreClass().createWorkout(this, workout)
        }*/
    }

    /*fun addExerciceToWorkout(exercise: Exercise) {
        val intent = Intent(this@ViewAllExercises, WorkoutListActivity::class.java)
        intent.putExtra("EXERCISE", exercise)
        startActivity(intent)

    }*/

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ViewAllExercises.CREATE_EXERCISE_REQUEST_CODE) {
            println("Activity result: " + resultCode)
            FireStoreClass().getExerciseList(this)
        }
        else {
            println("Did not enter if statemtn, resultcode: " + resultCode + " request code: " + requestCode)
            Log.e("Canceleld", "Cancelled")
        }
    }*/

    private fun setupActionBar() {
        setSupportActionBar(toolbar_all_user_activity)
        showProgressDialog(resources.getString(R.string.please_wait))
        val actionBar = supportActionBar
        showProgressDialog(resources.getString(R.string.please_wait))
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = "All Users"
        }
        toolbar_all_user_activity.setNavigationOnClickListener { onBackPressed() }
    }


        //ActionMode Start

    private val mActionModeCallback: androidx.appcompat.view.ActionMode.Callback = object : androidx.appcompat.view.ActionMode.Callback {

        override fun onCreateActionMode(mode: androidx.appcompat.view.ActionMode?, menu: Menu?): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater = mode?.menuInflater
            if (inflater != null) {
                inflater.inflate(R.menu.menu_multi_select, menu)
            }
            mode?.setTitle("Items choosed: ")
            return true
        }

        override fun onPrepareActionMode(mode: androidx.appcompat.view.ActionMode?, menu: Menu?): Boolean {
            return false // Return false if nothing is done
        }

        override fun onActionItemClicked(mode: androidx.appcompat.view.ActionMode?, item: MenuItem?): Boolean {
            if (item?.itemId == R.id.action_delete) {
                println("action delete pressed")
                mode?.finish()
                true
            }


            return false
        }

        override fun onDestroyActionMode(mode: androidx.appcompat.view.ActionMode?) {
            mActionMode = null
        }


    }









}

