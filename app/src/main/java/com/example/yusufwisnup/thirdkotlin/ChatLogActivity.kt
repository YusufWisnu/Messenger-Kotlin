package com.example.yusufwisnup.thirdkotlin

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.*

class ChatLogActivity : AppCompatActivity()
{
    lateinit var i:Intent
    val adapter = GroupAdapter<ViewHolder>()
    var toUser:User? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recycleview_chatlog.adapter = adapter
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.nama
        listenForMessage()

        send_button_chatlog.setOnClickListener {
            perfomSendMessage()
        }
    }

    private  fun listenForMessage(){
        val fromId = FirebaseAuth.getInstance().uid
        val toid = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toid")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?)
            {
                val chatmessage = p0.getValue(ChatMessage::class.java)
                if(chatmessage != null){
                    val currentUser = HomeActivity.currentUser?: return
                    if (chatmessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(ChatFromItem(chatmessage.text, currentUser))
                    }else{
                        adapter.add(ChatToItem(chatmessage.text, toUser!!))
                    }
                }
                recycleview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(p0: DatabaseError)
            {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?)
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

    private  fun perfomSendMessage()
    {
        val teks = edittext_chat_log.text.toString()

        val fromid = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toid = user.uid
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromid/$toid").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toid/$fromid").push()
        if(fromid == null)return

            val chatMessage = ChatMessage(reference.key!!, teks, fromid, toid, System.currentTimeMillis() / 1000)
            reference.setValue(chatMessage)
                    .addOnSuccessListener {
                        Log.d(TAG, "Successfully send the Message ${reference.key}")
                        edittext_chat_log.text.clear()
                        recycleview_chatlog.scrollToPosition(adapter.itemCount - 1)
                        val closeKeyboard = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        closeKeyboard.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                    }

            toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest_messages/$fromid/$toid")
        val latestMessagetoRef = FirebaseDatabase.getInstance().getReference("/latest_messages/$toid/$fromid")
        latestMessageRef.setValue(chatMessage)
        latestMessagetoRef.setValue(chatMessage)
    }

    companion object
    {
        val TAG = "ChatLog"
    }
}
