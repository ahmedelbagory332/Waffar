package bego.market.belbies.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Models.OrdersUser
import bego.market.belbies.R
import bego.market.belbies.ViewModel.Cart
import com.bumptech.glide.Glide


class UserOrderAdapter(val deleteOnClickListener: DeleteOnClickListener, val deliveryOnClickListener: DeliveryOnClickListener, private val context: Context?, var list: MutableList<OrdersUser>?) : RecyclerView.Adapter<UserOrderAdapter.ChooseSectionViewHolder>( ) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_user_order, parent, false)
        return ChooseSectionViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val order = list?.get(position)
         holder.orderProductName.text = "اسم المنتج : "+order?.name
         holder.orderProducttotalPrice.text = "اجمالى السعر : "+order?.price+" جنية "
        holder.orderProductNumber.text = "رقم الطلب : "+order?.productNumber
        holder.orderQuantity.text = "الكمية : "+order?.productQuantity

        Glide.with(context!!).load(order?.image).into(holder.orderproductImage)

        holder.deleteButton.setOnClickListener {
            deleteOnClickListener.clickListener(position)
        }
        holder.deliveryButton.setOnClickListener {
            deliveryOnClickListener.clickListener(order!!.productUserEmail,order.productNumber)
        }

    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var orderProductName: TextView = itemView.findViewById(R.id.order_name)
         var orderProducttotalPrice: TextView = itemView.findViewById(R.id.order_price)
        var orderProductNumber: TextView = itemView.findViewById(R.id.order_number)
        var orderQuantity: TextView = itemView.findViewById(R.id.order_count)
        var orderproductImage: ImageView = itemView.findViewById(R.id.order_imageView)
        var deleteButton: Button = itemView.findViewById(R.id.order_delete)
        var deliveryButton: Button = itemView.findViewById(R.id.order_delivery)

    }

    class DeliveryOnClickListener(val clickListener: (email: String,productNumber: String) -> Unit) {
    }

    class DeleteOnClickListener(val clickListener: (id: Int) -> Unit) {
    }



    fun search(newlist: MutableList<OrdersUser>) {
        list = ArrayList()
        list!!.addAll(newlist)
        notifyDataSetChanged()
    }

}
