package com.example.yusufwisnup.thirdkotlin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

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
                val progresbar : ProgressDialog = ProgressDialog.show(this, "Login", "Please Wait")
                //var progresbar : ProgressDialog = ProgressDialog(this, R.style.MyProgressBar)
                //progresbar.setTitle("Login")
                //progresbar.setMessage("Please Wait")
                progresbar.setCancelable(false)
                val ref = FirebaseAuth.getInstance()
                ref.signInWithEmailAndPassword(email, pwd).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        progresbar.hide()
                        i = Intent(this, HomeActivity::class.java)
                        startActivity(i)
                    }
                }.addOnFailureListener {
                    progresbar.hide()
                    Toast.makeText(this, "The User not Found", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}


