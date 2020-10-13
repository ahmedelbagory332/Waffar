package bego.market.belbies.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.R
import bego.market.belbies.ViewModel.Cart
import com.bumptech.glide.Glide


class CartAdapter(val deleteOnClickListener: DeleteOnClickListener,val detalisOnClickListener: OrderOnClickListener,private val context: Context?) : ListAdapter<Cart,CartAdapter.ChooseSectionViewHolder>(CartDiffCallback()) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_cart, parent, false)
        return ChooseSectionViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val section = getItem(position)
         holder.cartProductName.text =  "اسم المنتج : "+section.productName
         holder.cartproductQuantity.text = "الكمية : "+section.productQuantity
         holder.cartProducttotalPrice.text = "اجمالى السعر : "+section.totalPrice+" جنية "
        Glide.with(context!!).load(section.productImage).into(holder.cartproductImage)


        holder.deleteButton.setOnClickListener {
            deleteOnClickListener.clickListener(position)
        }
        holder.detailsButton.setOnClickListener {
            detalisOnClickListener.clickListener(section)
        }
    }



    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
         var cartProductName: TextView = itemView.findViewById(R.id.tv_cart_name)
         var cartproductQuantity: TextView = itemView.findViewById(R.id.tv_cart_count)
         var cartProducttotalPrice: TextView = itemView.findViewById(R.id.tv_cart_price)
         var cartproductImage: ImageView = itemView.findViewById(R.id.tv_cart_imageView)
        var deleteButton: Button = itemView.findViewById(R.id.button_delete)
        var detailsButton: Button = itemView.findViewById(R.id.button_detalis)

    }

    class OrderOnClickListener(val clickListener: (cart: Cart) -> Unit) {
    }

    class DeleteOnClickListener(val clickListener: (id: Int) -> Unit) {
    }

    class CartDiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }

    }


}
