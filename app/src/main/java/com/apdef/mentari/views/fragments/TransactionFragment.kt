package com.apdef.mentari.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apdef.mentari.R
import com.apdef.mentari.models.Transaction
import com.apdef.mentari.storage.SharedPref
import com.apdef.mentari.views.activities.transaction.TransactionDetailActivity
import com.apdef.mentari.views.adapters.TransactionAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_transaction.*

class TransactionFragment : Fragment() {
    lateinit var dbRef: DatabaseReference
    private var userAuth = FirebaseAuth.getInstance().currentUser
    private var listTransaction= ArrayList<com.apdef.mentari.models.Transaction>()
    private lateinit var pref: SharedPref
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val uid = userAuth?.uid.toString()
        super.onActivityCreated(savedInstanceState)
        pref = SharedPref(this!!.context!!)
        val username = pref.getValues("username")
        dbRef = FirebaseDatabase.getInstance().getReference("transaction")
            .child(uid)
        getListTransaction()
    }

    private fun getListTransaction(){
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+ error.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listTransaction.clear()
                for (getDataSnapshot in snapshot.children){
                    val transaction = getDataSnapshot.getValue(com.apdef.mentari.models.Transaction::class.java)
                    listTransaction.add(transaction!!)
                }
                showListTransaction(listTransaction)
            }

        })
    }

    private fun showListTransaction(data: java.util.ArrayList<Transaction>){
        var rvTransaction = rv_transaction as RecyclerView?
        rvTransaction?.setHasFixedSize(true)
        rvTransaction?.layoutManager = LinearLayoutManager(context)
        val transactionAdapter = TransactionAdapter(data)
        rvTransaction?.adapter = transactionAdapter
        transactionAdapter.setOnItemClickCallback(object : TransactionAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Transaction) {
                showTransactionDetail(data)
            }

        })
    }

    private fun showTransactionDetail(data: Transaction){
        val i = Intent(context, TransactionDetailActivity::class.java)
        i.putExtra(TransactionDetailActivity.EXTRA_TRANSACTION, data)
        startActivity(i)
    }
}