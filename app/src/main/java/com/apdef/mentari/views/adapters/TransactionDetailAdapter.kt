package com.apdef.mentari.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apdef.mentari.R
import com.apdef.mentari.models.Product
import kotlinx.android.synthetic.main.item_cart_product.view.*

class TransactionDetailAdapter (var sembako: ArrayList<Product>): RecyclerView.Adapter<TransactionDetailAdapter.TransactionDetailVh>() {
    private var mContext : Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionDetailVh {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        mContext = parent.context
        return TransactionDetailVh(inflater)
    }

    override fun getItemCount(): Int = sembako.size

    override fun onBindViewHolder(holder: TransactionDetailVh, position: Int) {
        val item : Product = sembako[position]
        if (item.count!= null){
            holder.bind(item)
        }
    }

    inner class TransactionDetailVh(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(data: Product){
            setView(itemView, data)
        }
        private fun setView(view: View, data: Product){
            var setItemCount = data.count!!.toString()
            var setPrice = data.price!!.toString()
            var setTitle = data.name!!.toString()
            with(view){
                tv_title_product.text = setTitle
                tv_price_product.text = setItemCount +"kg x "+ setPrice
            }
        }

    }

}