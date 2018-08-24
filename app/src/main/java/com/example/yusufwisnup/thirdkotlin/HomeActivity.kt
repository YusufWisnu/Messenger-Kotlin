package com.example.yusufwisnup.thirdkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.catatan_messages.view.*
import kotlin.collections.HashMap

class HomeActivity : AppCompatActivity()
{

    lateinit var i:Intent
    val adapter = GroupAdapter<ViewHolder>()
    val latestMessageHashMap = HashMap<String, ChatMessage>()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        recyclerview_home_edit.adapter = adapter

        //membuat garis vertical pada recyclerView
        recyclerview_home_edit.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //set item click listener on your adapter
        adapter.setOnItemClickListener { item, view ->
            i = Intent(this, ChatLogActivity::class.java)
            val row = item as LatestMessageRow
            i.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(i)
        }
        listenForLatestMessages()
        fetchCurrentUser()
        verifyUserIsLogin()

        }

    private fun fetchCurrentUser()
    {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot)
            {
                currentUser = p0.getValue(User::class.java)
                Log.d("HomeActivity", "Current User : ${currentUser?.profilImageUri}")
            }

            override fun onCancelled(p0: DatabaseError)
            {

            }
        })
    }

    private fun listenForLatestMessages()
    {
        val fromid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest_messages/$fromid")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?)
            {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageHashMap[p0.key!!] = chatMessage
                refreshRecycleview()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?)
            {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageHashMap[p0.key!!] = chatMessage
                refreshRecycleview()
            }

            override fun onCancelled(p0: DatabaseError)
            {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?)
            {
            }

            override fun onChildRemoved(p0: DataSnapshot)
            {
            }
        })
    }

    private fun refreshRecycleview()
    {
        adapter.clear()
        latestMessageHashMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun verifyUserIsLogin()
    {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null)
        {
            i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
        Toast.makeText(this,"Login dengan uid : $uid", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when(item?.itemId){
            R.id.menu_new_message ->{
            i = Intent(this, NewMessageActivity::class.java)
                startActivity(i)
            }
            R.id.menu_sign_out ->{
                FirebaseAuth.getInstance().signOut()
                i = Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object
    {
        var currentUser : User? = null

    }
}
