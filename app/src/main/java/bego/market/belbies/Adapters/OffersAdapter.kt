package bego.market.belbies.Adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Models.Offer
import bego.market.belbies.R
import com.bumptech.glide.Glide


class OffersAdapter(private val context: Context?, var list: MutableList<Offer>?, val onClickListener: OnClickListener, val buttonOnClickListener: ButtonOnClickListener) : RecyclerView.Adapter<OffersAdapter.ChooseSectionViewHolder>( ) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_offer, parent, false)
        return ChooseSectionViewHolder(view)
    }


    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val products = list?.get(position)
        if (products?.productOfferPercentage == "0" || products?.productOfferPrice == "0")
        {

            holder.productName.text = products.name
            holder.productPrice.text = products.price+" جنية "
            holder.productOfferPercentage.visibility = View.GONE
            holder.productOfferPrice.visibility = View.GONE
            holder.productPrice.paintFlags = holder.productPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        }
        else
        {
            holder.productName.text = products?.name
            holder.productPrice.text = products?.price+" جنية "
            holder.productOfferPercentage.text = products?.productOfferPercentage+" خصم "
            holder.productOfferPrice.text = products?.productOfferPrice+" جنية "
            holder.productPrice.paintFlags = holder.productPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.productOfferPercentage.visibility = View.VISIBLE
            holder.productOfferPrice.visibility = View.VISIBLE

        }
        Glide.with(context!!).load(products?.image).into(holder.productImage)

        holder.itemView.setOnClickListener {
            onClickListener.clickListener(products!!.id)
        }
        holder.cartButton.setOnClickListener {
            buttonOnClickListener.buttonClickListener(products!!)
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var productImage: ImageView = itemView.findViewById(R.id.product_img)
        var productName: TextView = itemView.findViewById(R.id.product_name)
        var productPrice: TextView = itemView.findViewById(R.id.product_price)
        var productOfferPrice: TextView = itemView.findViewById(R.id.product_offer_price)
        var productOfferPercentage: TextView = itemView.findViewById(R.id.product_offer_Percentage)
        var cartButton: ImageButton = itemView.findViewById(R.id.cart_button_offer)

    }

    class OnClickListener(val clickListener: (id: String) -> Unit) {
    }
    class ButtonOnClickListener(val buttonClickListener: (section: Offer) -> Unit) {

    }

    fun search(newlist: MutableList<Offer>) {
        list = ArrayList()
        list!!.addAll(newlist)
        notifyDataSetChanged()
    }

}
