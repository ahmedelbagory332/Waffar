package bego.market.waffar.adapters

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.R
import bego.market.waffar.models.AllProduct
import com.bumptech.glide.Glide
import javax.inject.Inject


class AdminProductAdapter @Inject constructor(val context: Application): RecyclerView.Adapter<AdminProductAdapter.ChooseSectionViewHolder>( ) {

    var onProductClick: ((products: AllProduct) -> Unit)? = null
    private var productsList = mutableListOf<AllProduct>()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_product_admin, parent, false)
        return ChooseSectionViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val products = productsList[position]
        if (products.productOfferPercentage == "0" || products.productOfferPrice == "0")
        {

            holder.productName.text = products.name
            holder.productPrice.text = products.price+" جنية "
            holder.productOfferPercentage.visibility = View.GONE
            holder.productOfferPrice.visibility = View.GONE
            holder.productPrice.paintFlags = holder.productPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        }
        else
        {
            holder.productName.text = products.name
            holder.productPrice.text = products.price+" جنية "
            holder.productOfferPercentage.text = products.productOfferPercentage+" خصم "
            holder.productOfferPrice.text = products.productOfferPrice+" جنية "
            holder.productPrice.paintFlags = holder.productPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.productOfferPercentage.visibility = View.VISIBLE
            holder.productOfferPrice.visibility = View.VISIBLE

        }

        Glide.with(context).load(products.image).into(holder.productImage)

        holder.itemView.setOnClickListener {
            onProductClick!!.invoke(products)
        }

    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var productImage: ImageView = itemView.findViewById(R.id.product_img)
        var productName: TextView = itemView.findViewById(R.id.product_name)
        var productPrice: TextView = itemView.findViewById(R.id.product_price)
        var productOfferPrice: TextView = itemView.findViewById(R.id.product_offer_price)
        var productOfferPercentage: TextView = itemView.findViewById(R.id.product_offer_Percentage)


    }


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<AllProduct>){
        productsList = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun search(newList: MutableList<AllProduct>) {
        productsList = ArrayList()
        productsList.addAll(newList)
        notifyDataSetChanged()
    }

}
