package com.apdef.mentari.views.interfaces

import android.os.Bundle
import androidx.fragment.app.Fragment

interface MainView : FragmentView {
    fun onShowFragment(mFragment: Fragment, savedInstanceState: Bundle?)
}