package bego.market.waffar.adapters

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.models.UserOrder
import bego.market.waffar.R
import com.bumptech.glide.Glide
import javax.inject.Inject


class UserOrdersAdapter @Inject constructor(val context: Application) : RecyclerView.Adapter<UserOrdersAdapter.ChooseSectionViewHolder>( ) {

    var deleteButtonClick: ((productPosition: Int) -> Unit)? = null
    var deliveryButtonClick: ((email: String,productNumber: String) -> Unit)? = null
    private var ordersList = mutableListOf<UserOrder>()





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_user_order, parent, false)
        return ChooseSectionViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val order = ordersList[position]

         holder.orderProductName.text = "اسم المنتج : "+order.name
         holder.orderProductTotalPrice.text = "اجمالى السعر : "+order.price+" جنية "
        holder.orderProductNumber.text = "رقم الطلب : "+order.productNumber
        holder.orderQuantity.text = "الكمية : "+order.productQuantity

        Glide.with(context).load(order.image).into(holder.orderProductImage)

        holder.deleteButton.setOnClickListener {
            deleteButtonClick!!.invoke(position)
        }
        holder.deliveryButton.setOnClickListener {
            deliveryButtonClick!!.invoke(order.productUserEmail,order.productNumber)
        }

    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var orderProductName: TextView = itemView.findViewById(R.id.order_name)
        var orderProductTotalPrice: TextView = itemView.findViewById(R.id.order_price)
        var orderProductNumber: TextView = itemView.findViewById(R.id.order_number)
        var orderQuantity: TextView = itemView.findViewById(R.id.order_count)
        var orderProductImage: ImageView = itemView.findViewById(R.id.order_imageView)
        var deleteButton: Button = itemView.findViewById(R.id.order_delete)
        var deliveryButton: Button = itemView.findViewById(R.id.order_delivery)

    }



    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<UserOrder>){
        ordersList = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun search(newList: MutableList<UserOrder>) {
        ordersList = ArrayList()
        ordersList.addAll(newList)
        notifyDataSetChanged()
    }
}
