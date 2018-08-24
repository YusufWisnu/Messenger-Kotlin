package com.example.yusufwisnup.thirdkotlin

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*


class ChatFromItem(val teks:String, val user:User): Item<ViewHolder>()
{
    override fun bind(viewHolder: ViewHolder, position: Int)
    {
        viewHolder.itemView.textview_from_row.text = teks
        val uri = user.profilImageUri
        val targetImageView = viewHolder.itemView.imageview_chat_from_row
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int
    {
        return R.layout.chat_from_row
    }
}