package com.example.yusufwisnup.thirdkotlin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.catatan_messages.view.*

class LatestMessageRow(val chatMessage:ChatMessage): Item<ViewHolder>(){
    var chatPartnerUser : User ?= null
    override fun bind(viewHolder: ViewHolder, position: Int)
    {
        viewHolder.itemView.textview_catatan_message.text = chatMessage.text
        val chatPartnerId : String
        if(chatMessage.fromId == FirebaseAuth.getInstance().uid)
        {
            chatPartnerId = chatMessage.toId
        }else{
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.keepSynced(true)
        ref.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                chatPartnerUser= p0.getValue(User::class.java)
                viewHolder.itemView.textview_nama_message.text = chatPartnerUser?.nama
                val targetImageView = viewHolder.itemView.imageview_catatan_message
                Picasso.get().load(chatPartnerUser?.profilImageUri).into(targetImageView)
            }

            override fun onCancelled(p0: DatabaseError)
            {

            }
        })

    }
    override fun getLayout(): Int
    {
        return R.layout.catatan_messages
    }
}
