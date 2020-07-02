package com.apdef.mentari.presenters

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.apdef.mentari.views.interfaces.HandlerView
import com.apdef.mentari.views.interfaces.MainView

class MainPresenters :
    HandlerView<MainView> {
    private var mView : MainView?  = null
    override fun onAttach(view: MainView) {
        mView = view
    }
    override fun onDetach() {
        mView = null
    }
    fun showFragment(mFragment: Fragment, savedInstanceState: Bundle?){
        mView?.onShowFragment(mFragment, savedInstanceState)
    }

}