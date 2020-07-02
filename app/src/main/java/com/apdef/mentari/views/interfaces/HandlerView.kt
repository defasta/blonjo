package com.apdef.mentari.views.interfaces

interface HandlerView <T:FragmentView>{
    fun onAttach(view:T)
    fun onDetach()
}