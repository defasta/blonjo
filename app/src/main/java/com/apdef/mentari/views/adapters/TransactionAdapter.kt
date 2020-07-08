package com.apdef.mentari.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apdef.mentari.R
import com.apdef.mentari.models.Transaction
import com.apdef.mentari.views.Utils.Companion.rupiah
import kotlinx.android.synthetic.main.item_list.view.*

class TransactionAdapter(var transaction: ArrayList<Transaction>): RecyclerView.Adapter<TransactionAdapter.TransactionVh>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    private var mContext : Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionVh {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        mContext = parent.context
        return TransactionVh(inflater)
    }

    override fun getItemCount(): Int = transaction.size

    override fun onBindViewHolder(holder: TransactionVh, position: Int) {
        val item : Transaction = transaction[position]
            holder.bind(item)

    }

    inner class TransactionVh(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(data: Transaction){
            setView(itemView, data)
            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(data)
            }
        }
        private fun setView(view: View, data: Transaction){
//            var setItemCount = data.count!!.toString()
//            var setPrice = data.price!!.toString()
//            var setTitle = data.name!!.toString()
            with(view){
                tv_date.text = data.time.toString()
                tv_title.text = rupiah(data.total.toString().toDouble())
                //tv_price_product.text = setItemCount +"kg x "+ setPrice
            }
        }

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: Transaction)
    }
}