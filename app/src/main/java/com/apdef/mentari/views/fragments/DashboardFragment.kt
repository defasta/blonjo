package com.apdef.mentari.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apdef.mentari.R
import com.apdef.mentari.models.User
import com.apdef.mentari.storage.SharedPref
import com.apdef.mentari.views.activities.sembako.SembakoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {
    private var userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var db: DatabaseReference
    lateinit var dbRef: DatabaseReference
    private lateinit var pref: SharedPref
    private var listUser= ArrayList<User>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pref = SharedPref(activity!!.applicationContext)
        val uid = userAuth?.uid.toString()
        dbRef = FirebaseDatabase.getInstance().getReference("user")
            .child(uid)


        btn_buy_product.setOnClickListener {
            val i = Intent(context, SembakoActivity::class.java)
            startActivity(i)
        }


    }

}