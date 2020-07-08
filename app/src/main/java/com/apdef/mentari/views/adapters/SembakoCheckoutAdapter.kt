package com.apdef.mentari.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apdef.mentari.R
import com.apdef.mentari.models.Product
import com.apdef.mentari.views.Utils.Companion.rupiah
import kotlinx.android.synthetic.main.item_cart_product.view.*

class SembakoCheckoutAdapter(var sembako: ArrayList<Product>):RecyclerView.Adapter<SembakoCheckoutAdapter.SembakoCheckoutVh>() {
    private var mContext : Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SembakoCheckoutVh {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        mContext = parent.context
        return SembakoCheckoutVh(inflater)
    }

    override fun getItemCount(): Int = sembako.size

    override fun onBindViewHolder(holder: SembakoCheckoutVh, position: Int) {
        val item : Product = sembako[position]
        if (item.count!= null){
            holder.bind(item)
        }
    }

    inner class SembakoCheckoutVh(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(data: Product){
            setView(itemView, data)
        }
        private fun setView(view: View, data: Product){
            var setItemCount = data.count!!.toString()
            var setPrice = data.price!!.toString()
            var setTitle = data.name!!.toString()
            var price = data.count!! * data.price!!
            with(view){
                tv_title_product.text = setTitle
                tv_price_product.text =  rupiah(price.toDouble())
                tv_qty.text = setItemCount +"kg"
            }
        }

    }

}