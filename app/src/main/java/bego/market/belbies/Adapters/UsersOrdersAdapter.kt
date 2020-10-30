package bego.market.belbies.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Models.UsersOrder

import bego.market.belbies.R


class UsersOrdersAdapter(private val context: Context?, var list: MutableList<UsersOrder>?, val onClickListener: OnClickListener, val deleteOnClickListener: DeleteOnClickListener) : RecyclerView.Adapter<UsersOrdersAdapter.ChooseSectionViewHolder>( ) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_users_orders, parent, false)
        return ChooseSectionViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val section = list?.get(position)
         holder.orderUserName.text = section?.userName
         holder.numberOrder.text = " عدد طلبات :  "+section?.orderNumbers
        holder.userAddress.text = section?.userAddress

        holder.itemView.setOnClickListener {
            onClickListener.clickListener(section!!.mail,section!!.userAddress,section!!.userPhone,section!!.userName)
        }
        holder.deleteButton.setOnClickListener {
            deleteOnClickListener.clickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var orderUserName: TextView = itemView.findViewById(R.id.order_user_name)
        var numberOrder: TextView = itemView.findViewById(R.id.number_order)
        var userAddress: TextView = itemView.findViewById(R.id.user_address)
        var deleteButton: Button = itemView.findViewById(R.id.user_delete)

    }

    class OnClickListener(val clickListener: (mail: String,address: String,phone: String,name: String) -> Unit) {
    }
    class DeleteOnClickListener(val clickListener: (UserOrder: Int) -> Unit) {
    }
    fun search(newlist: MutableList<UsersOrder>) {
        list = ArrayList()
        list!!.addAll(newlist)
        notifyDataSetChanged()
    }

}
