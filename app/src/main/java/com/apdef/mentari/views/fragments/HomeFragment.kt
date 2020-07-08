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
import com.apdef.mentari.models.Product
import com.apdef.mentari.storage.SharedPref
import com.apdef.mentari.views.Utils.Companion.rupiah
import com.apdef.mentari.views.activities.topup.TopupActivity
import com.apdef.mentari.views.activities.sembako.SembakoCheckoutActivity
import com.apdef.mentari.views.adapters.SembakoAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.img_profile
import kotlinx.android.synthetic.main.fragment_profile.*

class HomeFragment : Fragment() {
    private lateinit var pref: SharedPref
    lateinit var dbRef: DatabaseReference
    private var listSembako= ArrayList<Product>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pref = SharedPref(activity!!.applicationContext)
        tv_name.text = pref.getValues("username")
        tv_saldo.text = rupiah(pref.getValues("saldo").toString().toDouble())

        cv_saldo.setOnClickListener {
            val i = Intent(context, TopupActivity::class.java)
            startActivity(i)
        }

        fab_checkout.setOnClickListener {
            val i = Intent(context, SembakoCheckoutActivity::class.java)
            startActivity(i)
        }
        Glide.with(this)
            .load(pref.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(img_profile)

        dbRef = FirebaseDatabase.getInstance().getReference("order")
            .child("sembako")
        getSembako()
    }

    private fun getSembako(){
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+ error.message, Toast.LENGTH_LONG).show()
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
        var rvSembako = rv_product as RecyclerView?
        rvSembako?.setHasFixedSize(true)
        rvSembako?.layoutManager = LinearLayoutManager(context)
        val sembakoAdapter = SembakoAdapter(product)
        rvSembako?.adapter = sembakoAdapter
        sembakoAdapter.setOnClickItem(object : SembakoAdapter.ItemClick{
            override fun itemOnClicked(product: Product) {
                TODO("Not yet implemented")
            }

        })
    }


}