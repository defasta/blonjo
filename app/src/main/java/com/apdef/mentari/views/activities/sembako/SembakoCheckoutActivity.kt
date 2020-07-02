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
import com.apdef.mentari.storage.AppDatabase
import com.apdef.mentari.storage.SharedPref
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
    private lateinit var db: DatabaseReference
    //private lateinit var dbUser:DatabaseReference
    private lateinit var pref: SharedPref
    private var userAuth = FirebaseAuth.getInstance().currentUser
    private var mContext: Context? = null
    private var total = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sembako_checkout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        db = FirebaseDatabase.getInstance().getReference("transaction")
        pref = SharedPref(this)
        showCart()
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
                tv_charge.text = "Rp. "+ total.toString()
            }
        }
        btn_process.setOnClickListener {
            getTime(cartData, total)
            val i = Intent(this@SembakoCheckoutActivity, MainActivity::class.java)
            startActivity(i)
            finish()
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
        db.child(uid).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SembakoCheckoutActivity, "error:"+ error.message, Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                cartData.forEach {
                    it.time = time
                    db.child(uid).child(time).child("time").setValue(time)
                    db.child(uid).child(time).child("username").setValue(username)
                    db.child(uid).child(time).child("data").child(it.id).setValue(it)
                    db.child(uid).child(time).child("total").setValue(total)
                }
                Toast.makeText(applicationContext, "Transaksi berhasil!", Toast.LENGTH_LONG).show()
            }
        })


    }
}