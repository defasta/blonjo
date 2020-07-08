package com.apdef.mentari.views.activities.topup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.apdef.mentari.MainActivity
import com.apdef.mentari.R
import com.apdef.mentari.models.Topup
import com.apdef.mentari.models.User
import com.apdef.mentari.storage.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_topup.*
import kotlinx.android.synthetic.main.activity_topup.toolbar

class TopupActivity : AppCompatActivity() {
    private lateinit var kode: String
    private lateinit var dbTopup: DatabaseReference
    private lateinit var dbUser: DatabaseReference
    private lateinit var pref: SharedPref
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbUser = FirebaseDatabase.getInstance().getReference("user")
        dbTopup = FirebaseDatabase.getInstance().getReference("topup")
        pref = SharedPref(this)
        mAuth = FirebaseAuth.getInstance()

        val userAuth = mAuth.currentUser
        btn_cek.setOnClickListener{
            kode = et_code.text.toString()
            if(kode.equals("")){
                et_code.error = "Masukkan kode"
                et_code.requestFocus()
            }else{
                val token  = userAuth?.uid.toString()
                dbUser.child(token).removeValue()
                getSaldo(kode)
            }
        }
    }

    private fun getSaldo(kode: String){
        val userAuth = mAuth.currentUser
        val token  = userAuth?.uid.toString()
        val saldoNow = pref.getValues("saldo").toString().toInt()


        dbTopup.child(kode).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Kode tidak valid!", Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val topup = snapshot.getValue(Topup::class.java)
                if(topup== null){
                    Toast.makeText(applicationContext, "Kode tidak valid!", Toast.LENGTH_LONG).show()
                }else{
                    val saldoTopup = topup.total!!.toInt()
                    val saldoAkhir = saldoNow + saldoTopup
                    val data = User()
                    data.saldo = saldoAkhir
                    data.username = pref.getValues("username")
                    data.email = pref.getValues("email")
                    data.token = token
                    dbUser.child(token).setValue(data)
                    pref.setValues("saldo", saldoAkhir.toString())
                    Toast.makeText(applicationContext, "Top up berhasil!",Toast.LENGTH_LONG).show()
                    val i = Intent(this@TopupActivity, MainActivity::class.java)
                    startActivity(i)
                    finishAffinity()

//                    dbUser.child(token).child("saldo").addChildEventListener(object : ChildEventListener{
//                        override fun onCancelled(error: DatabaseError) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun onChildMoved(
//                            snapshot: DataSnapshot,
//                            previousChildName: String?
//                        ) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun onChildChanged(
//                            snapshot: DataSnapshot,
//                            previousChildName: String?
//                        ) {
//                            dbUser.child(token).child("saldo").setValue(saldoAkhir)
//                            pref.setValues("saldo", saldoAkhir.toString())
//                            Toast.makeText(applicationContext, "Top up berhasil!",Toast.LENGTH_LONG).show()
//                            val i = Intent(this@TopupActivity, MainActivity::class.java)
//                            startActivity(i)
//                            finishAffinity()
//                        }
//
//                        override fun onChildAdded(
//                            snapshot: DataSnapshot,
//                            previousChildName: String?
//                        ) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun onChildRemoved(snapshot: DataSnapshot) {
//                            TODO("Not yet implemented")
//                        }

//                    })

//                    dbUser.child(token).addValueEventListener(object : ValueEventListener{
//                        override fun onCancelled(error: DatabaseError) {
//                            Toast.makeText(applicationContext, "Top up gagal, silahkan coba lagi", Toast.LENGTH_LONG).show()
//                        }
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            val data = User()
//                            data.saldo = saldoAkhir
//                            data.username = pref.getValues("username")
//                            data.email = pref.getValues("email")
//                            data.token = token
////                            val user = snapshot.getValue(User::class.java)
////                            if(user==null){
////                                Toast.makeText(applicationContext, "Username tidak ditemukan!", Toast.LENGTH_LONG).show()
////                            }else{
//                                dbUser.child(token).setValue(data)
//                                pref.setValues("saldo", saldoAkhir.toString())
//                                Toast.makeText(applicationContext, "Top up berhasil!",Toast.LENGTH_LONG).show()
//                                val i = Intent(this@TopupActivity, MainActivity::class.java)
//                                startActivity(i)
//                                finishAffinity()
////                            }
//                        }
//
//                    })
                }
            }

        })
    }
}