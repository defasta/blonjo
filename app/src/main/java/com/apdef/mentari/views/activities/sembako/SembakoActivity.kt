package com.apdef.mentari.views.activities.sembako

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apdef.mentari.R
import com.apdef.mentari.models.Product
import com.apdef.mentari.storage.SharedPref
import com.apdef.mentari.views.adapters.SembakoAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sembako.*

class SembakoActivity : AppCompatActivity() {
    private lateinit var pref: SharedPref
    lateinit var dbRef: DatabaseReference
    private var listSembako= ArrayList<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sembako)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        pref = SharedPref(applicationContext)
        dbRef = FirebaseDatabase.getInstance().getReference("order")
            .child("sembako")
        getSembako()
        btn_checkout.setOnClickListener {
            val i = Intent(this, SembakoCheckoutActivity::class.java)
            startActivity(i)
        }
    }
    private fun getSembako(){
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SembakoActivity, ""+ error.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listSembako.clear()
                for (getDataSnapshot in snapshot.children){
                    val sembako = getDataSnapshot.getValue(Product::class.java)
                    listSembako.add(sembako!!)
                }
                showListSembako(listSembako)
            }

        })
    }

    private fun showListSembako(product: java.util.ArrayList<Product>){
        var rvSembako = rv_sembako as RecyclerView?
        rvSembako?.setHasFixedSize(true)
        rvSembako?.layoutManager = LinearLayoutManager(this)
        val sembakoAdapter = SembakoAdapter(product)
        rvSembako?.adapter = sembakoAdapter
        sembakoAdapter.setOnClickItem(object :SembakoAdapter.ItemClick{
            override fun itemOnClicked(product: Product) {
                TODO("Not yet implemented")
            }

        })
    }
}