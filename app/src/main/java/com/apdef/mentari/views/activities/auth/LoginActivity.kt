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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private var userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var db: DatabaseReference
    private lateinit var pref: SharedPref
    private lateinit var username: String
    private lateinit var password : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = FirebaseDatabase.getInstance().getReference("user")
        pref = SharedPref(this)
        mAuth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {
            username = etUsername.text.toString()
            password = etPassword.text.toString()

            if(username.equals("")){
                etUsername.error = "Masukkan email"
                etUsername.requestFocus()
            }
            else if(password.equals("")){
                etPassword.error = "Masukkan password"
                etPassword.requestFocus()
            }else{
                login(username, password)
            }
        }

        if(pref.getValues("status").equals("1")){
            finishAffinity()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            Toast.makeText(this@LoginActivity, "Welcome!", Toast.LENGTH_LONG).show()
        }

        btnToSignUp.setOnClickListener {
            val i = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(i)
        }

    }

    private fun login(email: String, pass: String){
        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val userAuth = mAuth.currentUser
                    val token  = userAuth?.uid.toString()

                    db.child(token).addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "Registrasi gagal!", Toast.LENGTH_LONG).show()
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if(user== null){
                                Toast.makeText(applicationContext, "Username tidak ditemukan!", Toast.LENGTH_LONG).show()
                            }else{
                                pref.setValues("username", user.username.toString())
                                pref.setValues("saldo", user.saldo.toString())
                                pref.setValues("email", user.email.toString())
                                pref.setValues("status", "1")
                                val i = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(i)
                                Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_LONG).show()
                            }
                        }

                    })
                    pref.setValues("status", "1")
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    Toast.makeText(this@LoginActivity, "Welcome!", Toast.LENGTH_LONG).show()
                    pref.setValues("status", "1")
                }else{
                    Toast.makeText(this@LoginActivity, "Email tidak ditemukan!", Toast.LENGTH_LONG).show()
                }
            }

   }
}