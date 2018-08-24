package com.example.yusufwisnup.thirdkotlin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class MainActivity : AppCompatActivity()
{
    lateinit var i: Intent
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        signup_tv.setOnClickListener() {
            i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }

        login_button.setOnClickListener {
            val email = email_login.text.toString()
            val pwd = password_login.text.toString()
            if (email.isEmpty())
            {
                Toast.makeText(this, "Please Fill the Email", Toast.LENGTH_SHORT).show()
            } else if (pwd.isEmpty())
            {
                Toast.makeText(this, "Please Fill the Password", Toast.LENGTH_SHORT).show()
            } else
            {
                val ref = FirebaseAuth.getInstance()
                ref.signInWithEmailAndPassword(email, pwd).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        i = Intent(this, HomeActivity::class.java)
                        startActivity(i)
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "The User not Found", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}


