package com.example.yusufwisnup.thirdkotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.ScriptGroup
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity()
{
    lateinit var i:Intent
    var selectedphotouri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        //SELECT FOTO
        select_foto_regist_button.setOnClickListener(){
            i = Intent(Intent.ACTION_PICK)
            i.setType("image/*")
            startActivityForResult(i, 0)
        }



        //REGISTER BUTTON
        conf_regist_button.setOnClickListener(){
            val email = email_edittext.text.toString()
            val pwd = password_edittext.text.toString()

            //pattern buat check number dalam string
            val regex = Regex(".*\\d+.*")

            if(email.isEmpty()){
                Toast.makeText(this, "Please Fill the Email", Toast.LENGTH_SHORT).show()
            }else if(pwd.isEmpty()){
                Toast.makeText(this, "Please Fill the Password", Toast.LENGTH_SHORT).show()
            }else if(!pwd.matches(regex))
            {
                Toast.makeText(this, "Password must contains number", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener {
                            if (!it.isSuccessful) return@addOnCompleteListener

                            uploadImagetoFirebaseStorage()
                            Toast.makeText(this, "Account was Successfully Created", Toast.LENGTH_SHORT).show()
                            Log.d("Main", "UID: ${it.result.user.uid}")
                            i = Intent(this, MainActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(i)

                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to Create User", Toast.LENGTH_SHORT).show()
                        }
            }
        }

        see_password.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                password_edittext.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                conf_pass_edittext.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }else{
                password_edittext.inputType = InputType.TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
                conf_pass_edittext.inputType = InputType.TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            selectedphotouri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedphotouri)
            circle_photo.setImageBitmap(bitmap)
            select_foto_regist_button.alpha = 0f        //buat background circle jadi nol float agar foto nampak
            //val bitmapdrawable = BitmapDrawable(bitmap)
            //select_foto_regist_button.setBackgroundDrawable(bitmapdrawable)
        }
    }

    private fun uploadImagetoFirebaseStorage(){
        if(selectedphotouri == null)return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedphotouri!!)
                .addOnSuccessListener {
                    Log.d("Register", "Name of Upload Image : ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Register", "File Location : $it")

                        saveuserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener{

                }
    }

    private fun saveuserToFirebaseDatabase(profilImageUri: String){
        val uid =  FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(nama_edittext.text.toString(), uid, username_edittext.text.toString(), email_edittext.text.toString() ,profilImageUri, password_edittext.text.toString())
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Register aa", "Finally we save the user to Firebase Database")
                }
                .addOnFailureListener{
                    Log.d("Register", "Gagal input ke database ${it.message}")
                }
    }
}
