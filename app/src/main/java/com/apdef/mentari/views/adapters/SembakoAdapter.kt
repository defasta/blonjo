package com.apdef.mentari.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apdef.mentari.R
import com.apdef.mentari.models.Product
import com.apdef.mentari.storage.AppDatabase
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list_product.view.*

class SembakoAdapter(val sembako: ArrayList<Product>):RecyclerView.Adapter<SembakoAdapter.SembakoVH>() {
    private var mContext: Context? = null
    private var itemClick: ItemClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SembakoAdapter.SembakoVH {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_list_product, parent, false)
        mContext = parent.context
        return SembakoVH(inflater)
    }

    override fun getItemCount(): Int = sembako.size

    override fun onBindViewHolder(holder: SembakoAdapter.SembakoVH, position: Int) {
        val item : Product = sembako[position]
        holder.bind(item)
    }

    inner class SembakoVH(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(data: Product){
            itemView.tv_title_product.text = data.name.toString()
            itemView.tv_price_product.text = data.price.toString()
            Glide.with(itemView).load(data.images).into(itemView.iv_product)
            if (data.count!=null) {
                if (data.count!!>0) {
                    itemView.btn_qty.visibility = View.VISIBLE
                    itemView.btn_buy_product.visibility = View.GONE
                    itemView.tv_jumlah.text = data.count.toString()
                    itemView.tv_price_product.text = setPriceItem(data.count!!, data.price!!).toString()
                } else {
                    itemView.btn_qty.visibility = View.GONE
                    itemView.btn_buy_product.visibility = View.VISIBLE
                }
            }

            setCount(itemView, data)

        }
        private fun setPriceItem(counterItem: Int, priceItem: Int): Int {
            return setPriceByCount(counterItem, priceItem)
        }
        private fun setPriceByCount(itemCount: Int, itemPrice: Int): Int {
            return itemCount * itemPrice
        }
        private fun setCount(view: View, data: Product) {
            var setItemCount = 0
            var setPrice = 0

            if (data.count!=null) {
                if (data.count!!>0) {
                    itemView.btn_qty.visibility = View.VISIBLE
                    itemView.btn_buy_product.visibility = View.GONE
                    setItemCount = data.count!!
                    setPrice = data.price!!.toInt()
                    view.btn_qty.visibility = View.VISIBLE
                    view.btn_buy_product.visibility = View.GONE
                } else {
                    itemView.btn_qty.visibility = View.GONE
                    itemView.btn_buy_product.visibility = View.VISIBLE
                }
            } else {
                setItemCount = 0
            }

            view.btn_buy_product.setOnClickListener {
                data.count = 1
                AppDatabase.getInstance(mContext!!)?.productDao()?.insertOrUpdate(data)
                setItemCount = data.count!!
                setPrice = data.price!!
                itemView.btn_qty.visibility = View.VISIBLE
                itemView.btn_buy_product.visibility = View.GONE
            }

            data.count = setItemCount
            AppDatabase.getInstance(mContext!!)?.productDao()?.insertOrUpdate(data)

            view.cv_plus.setOnClickListener {
                setItemCount++
                view.btn_qty.visibility = View.VISIBLE
                view.tv_jumlah.text = setItemCount.toString()
                view.tv_price_product.text = setPriceItem(setItemCount, setPrice).toString()
                data.count = setItemCount
                AppDatabase.getInstance(mContext!!)?.productDao()?.insertOrUpdate(data)
            }

            view.cv_min.setOnClickListener {
                setItemCount--
                if (setItemCount <= 0) {
                    view.btn_qty.visibility = View.GONE
                    view.btn_buy_product.visibility = View.VISIBLE
                } else {
                    view.tv_jumlah.text = setItemCount.toString()
                    view.tv_price_product.text =  setPriceItem(setItemCount, setPrice).toString()
                }
                data.count = setItemCount
                AppDatabase.getInstance(mContext!!)?.productDao()?.update(data)

            }
        }


    }
    fun setOnClickItem(itemClick: ItemClick){
        this.itemClick = itemClick
    }
    interface ItemClick{
        fun itemOnClicked(product:Product)
    }
}