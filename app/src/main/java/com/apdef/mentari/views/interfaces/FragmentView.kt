package com.apdef.mentari.views.interfaces

import android.os.Bundle

interface FragmentView {
    fun onAttachView(savedInstanceState: Bundle?)
    fun onDetachView()
}