package com.example.yusufwisnup.thirdkotlin

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class UserItem(val user:User):Item<ViewHolder>()
{

    override fun bind(viewHolder: ViewHolder, position: Int)
    {
        viewHolder.itemView.name_tv_new_message.text = user.nama

        Picasso.get().load(user.profilImageUri).into(viewHolder.itemView.imageview_new_message)
    }
    override fun getLayout(): Int
    {
        return R.layout.user_row_new_message
    }
}