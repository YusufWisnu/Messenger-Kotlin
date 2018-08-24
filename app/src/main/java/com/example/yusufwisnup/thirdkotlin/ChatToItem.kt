package com.example.yusufwisnup.thirdkotlin

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(val teks:String, val user:User): Item<ViewHolder>()
{
    override fun bind(viewHolder: ViewHolder, position: Int)
    {
        viewHolder.itemView.textview_to_row.text = teks

        //load our user image into the star
        val uri = user.profilImageUri
        val targetImageView = viewHolder.itemView.imageview_chat_to_row
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int
    {
        return R.layout.chat_to_row
    }
}