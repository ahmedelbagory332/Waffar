package bego.market.waffar.adapters

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.data.localDataBase.Cart
import com.bumptech.glide.Glide
import bego.market.waffar.R
import javax.inject.Inject


class CartAdapter @Inject constructor(val context: Application) : ListAdapter<Cart, CartAdapter.ChooseSectionViewHolder>(CartDiffCallback()) {

    var deleteButtonClick: ((id: Long,position: Int) -> Unit)? = null
    var orderClick: ((order: Cart,id: Int) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_cart, parent, false)
        return ChooseSectionViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val order = getItem(position)
         holder.cartProductName.text =  "اسم المنتج : "+order.productName
         holder.cartProductQuantity.text = "الكمية : "+order.productQuantity
         holder.cartProductTotalPrice.text = "اجمالى السعر : "+order.totalPrice+" جنية "
        Glide.with(context).load(order.productImage).into(holder.cartProductImage)


        holder.deleteButton.setOnClickListener {
            deleteButtonClick!!.invoke(order.id,position)
        }
        holder.orderButton.setOnClickListener {
            orderClick!!.invoke(order,position)
        }
    }



    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
         var cartProductName: TextView = itemView.findViewById(R.id.tv_cart_name)
         var cartProductQuantity: TextView = itemView.findViewById(R.id.tv_cart_count)
         var cartProductTotalPrice: TextView = itemView.findViewById(R.id.tv_cart_price)
         var cartProductImage: ImageView = itemView.findViewById(R.id.tv_cart_imageView)
        var deleteButton: Button = itemView.findViewById(R.id.button_delete)
        var orderButton: Button = itemView.findViewById(R.id.button_order)

    }


    class CartDiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.productId == newItem.productId
        }

    }


}
