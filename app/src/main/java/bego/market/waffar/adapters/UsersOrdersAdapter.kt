package bego.market.waffar.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.models.UsersOrder
import bego.market.waffar.R
import javax.inject.Inject


class UsersOrdersAdapter @Inject constructor() : RecyclerView.Adapter<UsersOrdersAdapter.ChooseSectionViewHolder>( ) {

    var deleteButtonClick: ((orderPosition: Int) -> Unit)? = null
    var orderClick: ((mail: String,address: String,phone: String,name: String) -> Unit)? = null
    private var ordersList = mutableListOf<UsersOrder>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_users_orders, parent, false)
        return ChooseSectionViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val usersOrder = ordersList[position]
         holder.orderUserName.text = usersOrder.userName
         holder.numberOrder.text = " عدد طلبات :  "+ usersOrder.orderNumbers
        holder.userAddress.text = usersOrder.userAddress

        holder.itemView.setOnClickListener {
            orderClick!!.invoke(usersOrder.mail,usersOrder.userAddress,usersOrder.userPhone,usersOrder.userName)
        }
        holder.deleteButton.setOnClickListener {
            deleteButtonClick!!.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var orderUserName: TextView = itemView.findViewById(R.id.order_user_name)
        var numberOrder: TextView = itemView.findViewById(R.id.number_order)
        var userAddress: TextView = itemView.findViewById(R.id.user_address)
        var deleteButton: Button = itemView.findViewById(R.id.user_delete)

    }


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<UsersOrder>){
        ordersList = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun search(newList: MutableList<UsersOrder>) {
        ordersList = ArrayList()
        ordersList.addAll(newList)
        notifyDataSetChanged()
    }

}
