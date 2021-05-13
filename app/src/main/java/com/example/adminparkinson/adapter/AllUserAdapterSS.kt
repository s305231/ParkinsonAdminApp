package com.example.adminparkinson.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminparkinson.Activities.MainViewModel
import com.example.adminparkinson.Activities.ViewAllUsers
import com.example.adminparkinson.R
import com.example.adminparkinson.model.User
import kotlinx.android.synthetic.main.item_all_exercises.view.*
import kotlinx.android.synthetic.main.item_all_users.view.*


open class AllUserAdapterSS(private val context: Context, private var list: ArrayList<User>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //private var onClickListener : AllUserAdapter.OnClickListener? = null
    private var onItemClick : AllUserAdapterSS.OnItemClick? = null
    private var isEnable : Boolean = false
    private var isSelectAll: Boolean = false
    var mActionMode: ActionMode? = null
    val selectUsersArrayList: ArrayList<String> = ArrayList()
    private var nr : Int = 0
    private var mainViewModel: MainViewModel? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AllUserAdapterSS.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_all_users, parent, false)
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is AllUserAdapterSS.MyViewHolder) {
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.itemView.civ_user_image)


            holder.itemView.tv_userName.text = model.name


            /*holder.itemView.btn_select_user.setOnClickListener{
                println("Select User Button Pressed")
                if (context is ViewAllUsers) {
                    //context.addExerciceToWorkout(model)
                }
            }*/

            holder.itemView.setOnLongClickListener {

                if (!isEnable) {
                    //when action mode is not enabled

                    val mActionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
                        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                            val inflater = mode?.menuInflater
                            if (inflater != null) {
                                inflater.inflate(R.menu.menu_multi_select, menu)
                            }
                            mode?.setTitle("Items choosed: " + selectUsersArrayList.size)
                            return true
                        }

                        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                            isEnable = true
                            ClickItem(holder)

                            return true
                        }

                        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {


                            when (item?.itemId) {
                                R.id.action_delete -> {
                                    print("Delete button pressed")
                                    for (s in selectUsersArrayList) {
                                        //Should be list.remove, but for now we dont have a delete method so
                                        println("assign users pressed, These user would be deleted: $s")
                                        selectUsersArrayList.remove(s)
                                    }
                                    mode?.finish()
                                    true
                                }
                                R.id.action_select_all -> {
                                    print("Select all button pressed")
                                    if (selectUsersArrayList.size == list.size) {
                                        //All users selected
                                        isSelectAll = false
                                        selectUsersArrayList.clear()

                                    } else {
                                        //No users selected
                                        isSelectAll = true
                                        selectUsersArrayList.clear()
                                        //?? 21:11
                                        selectUsersArrayList.addAll(listOf(model.id))
                                    }
                                    true
                                }
                                R.id.action_select_users -> {
                                    getSelectedUserList()
                                    mode?.finish()
                                    true
                                }
                            }





                            /*if (item?.itemId == R.id.action_select_users) {
                                getSelectedUserList()
                                for (s in selectUsersArrayList) {
                                    println("assign users pressed, These user would be deleted: $s")
                                    holder.itemView.setBackgroundColor(Color.TRANSPARENT)
                                }
                                diasbleSelect(holder)
                                selectUsersArrayList.clear()
                                notifyDataSetChanged()
                                mode?.finish()
                                true
                            }*/


                            return false
                        }

                        override fun onDestroyActionMode(mode: ActionMode?) {
                            isEnable = false
                            isSelectAll = false
                            selectUsersArrayList.clear()
                            notifyDataSetChanged()
                        }
                    }

                    holder.itemView.startActionMode(mActionModeCallback)



                    //onItemClick!!.onLongPress(position, model)
                } else {
                    ClickItem(holder)
                }

                return@setOnLongClickListener true
            }



            holder.itemView.setOnClickListener {

                if (isEnable) {
                    ClickItem(holder)
                } else {
                    println("You clicked: " + list[position].name)
                }

                /*if (onItemClick != null) {
                    println("From adapter list: " + selectUsersArrayList)
                    onItemClick!!.onItemClick(position, model)

                }*/

            }

            /*if (isSelectAll) {
                //When all values are selected
                holder.itemView.iv_check_box.visibility = View.VISIBLE
                holder.itemView.setBackgroundColor(Color.LTGRAY)
            } else {
                holder.itemView.iv_check_box.visibility = View.GONE
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }*/
        }
    }



    private fun ClickItem(holder: AllUserAdapterSS.MyViewHolder) {
        //Get selected item value
        val s : String = list[holder.adapterPosition].id
        //Check condition
        if (holder.itemView.iv_check_box.visibility == View.GONE) {
            //when item not selected
            holder.itemView.iv_check_box.visibility = View.VISIBLE
            //Set backgroundcolor
            holder.itemView.setBackgroundColor(Color.LTGRAY)
            selectUsersArrayList.add(s)
            selectedItemCount()

        }else {
            //When item selected
            //Hide checkbox img
            holder.itemView.iv_check_box.visibility = View.GONE
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            //REmove value from select user lsit
            selectUsersArrayList.remove(s)
            selectedItemCount()
        }

        mainViewModel?.setText(selectUsersArrayList.toString())


    }

    fun getSelectedUserList() {

        var selectedUserList = selectUsersArrayList
        ViewAllUsers().createWorkout(selectedUserList)



    }

     fun selectedItemCount() {
        nr = selectUsersArrayList.size
    }

    private fun diasbleSelect(holder: AllUserAdapterSS.MyViewHolder) {
        holder.itemView.iv_check_box.visibility = View.GONE
        holder.itemView.setBackgroundColor(Color.TRANSPARENT)
    }


    interface OnItemClick {
        fun onItemClick(position: Int, model: User)
        fun onLongPress(position: Int, model: User)
    }

    fun setOnItemClickListener(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }



    override fun getItemCount(): Int {
        return list.size
    }


    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)



}




