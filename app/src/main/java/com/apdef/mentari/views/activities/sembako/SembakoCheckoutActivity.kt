package com.apdef.mentari.views.activities.sembako

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.apdef.mentari.MainActivity
import com.apdef.mentari.R
import com.apdef.mentari.api.RetrofitClient
import com.apdef.mentari.models.ResponseTime
import com.apdef.mentari.models.Product
import com.apdef.mentari.models.Transaction
import com.apdef.mentari.models.User
import com.apdef.mentari.storage.AppDatabase
import com.apdef.mentari.storage.SharedPref
import com.apdef.mentari.views.Utils.Companion.rupiah
import com.apdef.mentari.views.adapters.SembakoCheckoutAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sembako.*
import kotlinx.android.synthetic.main.activity_sembako_checkout.*
import kotlinx.android.synthetic.main.activity_sembako_checkout.toolbar
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SembakoCheckoutActivity : AppCompatActivity() {
    private lateinit var dbTransaction: DatabaseReference
    private lateinit var dbUser:DatabaseReference
    private lateinit var pref: SharedPref
    private var userAuth = FirebaseAuth.getInstance().currentUser
    private var mContext: Context? = null
    private var total = 0
    private var listTransaction= ArrayList<com.apdef.mentari.models.Transaction>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sembako_checkout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dbTransaction = FirebaseDatabase.getInstance().getReference("transaction")
        dbUser = FirebaseDatabase.getInstance().getReference("user")
        pref = SharedPref(this)
        showCart()
        if (total == 0){
            btn_process.visibility = View.GONE
        }else{
            btn_process.visibility = View.VISIBLE
        }
    }

    private fun showCart(){
        val cartData  = AppDatabase.getInstance(this)?.productDao()?.getCart()
        if(cartData!!.isNotEmpty()){
            var rvCart = rv_cart
            rvCart?.setHasFixedSize(true)
            rvCart?.layoutManager = LinearLayoutManager(this)
            val sembakoCheckoutAdapter = SembakoCheckoutAdapter(cartData as ArrayList<Product>)
            rvCart.adapter = sembakoCheckoutAdapter
            sembakoCheckoutAdapter.notifyDataSetChanged()
            cartData.forEach{
                if(it.count!!>=1){
                    total += it.price!! * it.count!!
                }
                tv_charge.text = rupiah(total.toString().toDouble())
            }
        }
        val saldoNow = pref.getValues("saldo").toString().toInt()
        val saldoAkhir = saldoNow - total
        btn_process.setOnClickListener {
            if (saldoAkhir < 0){
                Toast.makeText(this, "Saldo tidak cukup!", Toast.LENGTH_LONG).show()
            }else {
                getTime(cartData, total)
            }
        }
    }

    private fun getTime(cartData: List<Product>, total:Int){
        val uid = userAuth?.uid.toString()
        RetrofitClient.instance.getTime("XAEEV5QFFFLL", "json", "zone", "Asia/Jakarta" )
            .enqueue(object : Callback<ResponseTime> {
                override fun onFailure(call: Call<ResponseTime>, t: Throwable) {
                    Log.e("ERROR FAILURE",t.toString())
                }

                override fun onResponse(
                    call: Call<ResponseTime>,
                    response: Response<ResponseTime>
                ) {
                    if (response.isSuccessful) {
                        try {
                            if (response.code() == 200) {
                                val time = response.body()?.formatted.toString()
                                saveData(time, cartData, uid, total)

                            } else {
                                Log.e("gagal muat", response.message().toString())
                            }
                        } catch (e: JSONException) {
                            Log.e("ERROR JSON", e.printStackTrace().toString())
                        } catch (e: IOException) {
                            Log.e("ERROR IO", e.printStackTrace().toString())
                        }
                    }
                }
            })
    }

    private fun saveData(time: String, cartData: List<Product>, uid:String, total: Int){
        val username = pref.getValues("username")
        val saldoNow = pref.getValues("saldo").toString().toInt()
        val saldoAkhir = saldoNow - total

        dbTransaction.child(uid).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SembakoCheckoutActivity, "error:"+ error.message, Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                cartData.forEach {
                    it.time = time
                    dbTransaction.child(uid).child(time).child("time").setValue(time)
                    dbTransaction.child(uid).child(time).child("username").setValue(username)
                    dbTransaction.child(uid).child(time).child("data").child(it.id).setValue(it)
                    dbTransaction.child(uid).child(time).child("total").setValue(total)
                }

                val data = User()
                data.saldo = saldoAkhir
                data.username = pref.getValues("username")
                data.email = pref.getValues("email")
                data.token = uid

                dbUser.child(uid).setValue(data)
                pref.setValues("saldo", saldoAkhir.toString())
                Toast.makeText(applicationContext, "Transaksi berhasil!",Toast.LENGTH_LONG).show()
                val i = Intent(this@SembakoCheckoutActivity, MainActivity::class.java)
                startActivity(i)
                finishAffinity()
//                dbUser.child(uid).addValueEventListener(object : ValueEventListener{
//                    override fun onCancelled(error: DatabaseError) {
//                        Toast.makeText(applicationContext, "Transaksi gagal, silahkan coba lagi", Toast.LENGTH_LONG).show()
//                    }
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val data = User()
//                        data.saldo = saldoAkhir
//                        data.username = pref.getValues("username")
//                        data.email = pref.getValues("email")
//                        data.token = uid
////                        val user = snapshot.getValue(User::class.java)
////                        if(user==null){
////                            Toast.makeText(applicationContext, "Username tidak ditemukan!", Toast.LENGTH_LONG).show()
////                        }else{
//                            dbUser.child(uid).setValue(data)
//                            pref.setValues("saldo", saldoAkhir.toString())
//                            Toast.makeText(applicationContext, "Transaksi berhasil!",Toast.LENGTH_LONG).show()
//                            val i = Intent(this@SembakoCheckoutActivity, MainActivity::class.java)
//                            startActivity(i)
//                            finishAffinity()
////                        }
//                    }
//                })
            }
        })



    }
}