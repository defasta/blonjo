package com.apdef.mentari.views.activities.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apdef.mentari.R
import com.apdef.mentari.models.Product
import com.apdef.mentari.models.Transaction
import com.apdef.mentari.storage.SharedPref
import com.apdef.mentari.views.adapters.TransactionDetailAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sembako.*
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import kotlinx.android.synthetic.main.activity_transaction_detail.toolbar

class TransactionDetailActivity : AppCompatActivity() {
    private var userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var pref: SharedPref
    lateinit var dbRef: DatabaseReference
    private var listProduct = ArrayList<Product>()
    companion object{
        const val EXTRA_TRANSACTION = "extra_transaction"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val transactionData = intent.getParcelableExtra(EXTRA_TRANSACTION) as Transaction
        pref = SharedPref(applicationContext)
        val uid = userAuth?.uid.toString()
        val username = pref.getValues("username")
        getTransactionDetail(transactionData.time, uid)

    }

    private fun getTransactionDetail(time: String, uid:String){
        dbRef = FirebaseDatabase.getInstance().getReference("transaction")
            .child(uid)
            .child(time)
            .child("data")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TransactionDetailActivity, ""+ error.message, Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                listProduct.clear()
                for (getDataSnapshot in snapshot.children){
                    val product = getDataSnapshot.getValue(Product::class.java)
                    listProduct.add(product!!)
                }
                showListProduct(listProduct)
            }

        })
    }

    private fun showListProduct(product: java.util.ArrayList<Product>){
        var rvTransaction = rv_transaction_detail as RecyclerView?
        rvTransaction?.setHasFixedSize(true)
        rvTransaction?.layoutManager = LinearLayoutManager(this)
        val transactionDetailAdapter = TransactionDetailAdapter(product)
        rvTransaction?.adapter = transactionDetailAdapter
    }
}