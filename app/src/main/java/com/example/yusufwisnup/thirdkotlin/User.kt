package com.example.yusufwisnup.thirdkotlin

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val nama:String, val uid: String, val username: String, val email:String, val profilImageUri:String, val pwd:String):Parcelable
{
    constructor():this("","","","","", "")
}