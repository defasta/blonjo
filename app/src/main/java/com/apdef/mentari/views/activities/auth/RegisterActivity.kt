package com.apdef.mentari.views.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.apdef.mentari.MainActivity
import com.apdef.mentari.R
import com.apdef.mentari.models.User
import com.apdef.mentari.storage.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    //private var userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String
    private lateinit var email: String
    private lateinit var db: DatabaseReference
    private lateinit var pref : SharedPref
    private lateinit var token: String
    private var user = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        pref = SharedPref(this)
        db = FirebaseDatabase.getInstance().getReference("user")
        mAuth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            if(etUsername.text.toString().equals("")){
                etUsername.error = "Username tidak boleh kosong!"
                etUsername.requestFocus()
            }else if (etEmail.text.toString().equals("")){
                etEmail.error = "Email tidak boleh kosong!"
                etEmail.requestFocus()
            }else if(etPassword.text.toString().equals("")){
                etPassword.error = "Password tidak boleh kosong!"
                etPassword.requestFocus()
            }else if(etPassConfirm.text.toString().equals("")){
                etPassConfirm.error = "Konfirmasi password tidak boleh kosong!"
                etPassConfirm.requestFocus()
            }else if(etPassConfirm.text.toString() != etPassword.text.toString()){
                etPassConfirm.error = "Password belum sama!"
                etPassConfirm.requestFocus()
            }else{
                username = etUsername.text.toString()
                password = etPassword.text.toString()
                passwordConfirm = etPassConfirm.text.toString()
                email = etEmail.text.toString()
                saveUser(username, email, password)

            }
        }
    }

    private fun saveUser(username:String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){

                    var currentUser = mAuth.currentUser
                    val token = currentUser?.uid.toString()
                    val data = User()
                    data.username = username
                    data.email = email
                    data.token = token
                    data.saldo = 0

                    db.child(token).addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "Registrasi gagal!", Toast.LENGTH_LONG).show()
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if(user== null){
                                db.child(token).setValue(data)
                                pref.setValues("username", data.username.toString())
                                pref.setValues("email", data.email.toString())
                                pref.setValues("saldo", data.saldo.toString())
                                pref.setValues("status", "1")
                                val i = Intent(this@RegisterActivity, MainActivity::class.java)
                                startActivity(i)
                                Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(applicationContext, "Email sudah terdaftar", Toast.LENGTH_LONG).show()
                            }
                        }

                    })
                }else{
                    Toast.makeText(applicationContext, "Email sudah terdaftar!", Toast.LENGTH_LONG).show()
                }
            }
    }




}