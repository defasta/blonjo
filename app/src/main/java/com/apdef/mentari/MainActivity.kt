package com.apdef.mentari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.apdef.mentari.presenters.MainPresenters
import com.apdef.mentari.views.fragments.DashboardFragment
import com.apdef.mentari.views.fragments.TransactionFragment
import com.apdef.mentari.views.fragments.ProfileFragment
import com.apdef.mentari.views.interfaces.MainView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {
    var presenter : MainPresenters? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPresenter()
        onAttachView(savedInstanceState)
    }
    private fun menuHandler(savedInstanceState: Bundle?){
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.nav_dashboard ->{
                    presenter?.showFragment(DashboardFragment(),savedInstanceState)
                }
                R.id.nav_order -> {
                    presenter?.showFragment(TransactionFragment(),savedInstanceState)
                }
            }
            true
        }
        bottom_navigation.selectedItemId = R.id.nav_dashboard
    }
    private fun initPresenter() {
        presenter = MainPresenters()
    }
    override fun onShowFragment(mFragment: Fragment, savedInstanceState: Bundle?) {
        if (savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container,mFragment, mFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onAttachView(savedInstanceState: Bundle?) {
        presenter?.onAttach(this)
        menuHandler(savedInstanceState)
    }

    override fun onDetachView() {
        onDetachView()
        super.onDestroy()
    }
}