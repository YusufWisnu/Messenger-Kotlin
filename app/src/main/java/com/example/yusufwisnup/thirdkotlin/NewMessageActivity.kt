package com.example.yusufwisnup.thirdkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity()
{
    lateinit var i:Intent
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        fetchUsers()
    }

    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot)
            {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    Log.d("Message", it.toString())
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }

                adapter.setOnItemClickListener{ item, view ->
                    val userItem = item as UserItem
                    i = Intent(view.context, ChatLogActivity::class.java)
                    i.putExtra(USER_KEY,userItem.user)
                    startActivity(i)
                    finish()

                }
                recycle_view_new_message.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError)
            {

            }


        })
    }

    companion object
    {
        val USER_KEY = "USER_KEY"
    }
}
