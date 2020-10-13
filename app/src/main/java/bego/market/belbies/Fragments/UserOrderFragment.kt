package bego.market.belbies.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.UserOrderAdapter
import bego.market.belbies.Models.OrdersUser
import bego.market.belbies.Models.AllUsersOrders
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.ViewModel.*
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.github.ybq.android.spinkit.SpinKitView
import com.sdsmdg.tastytoast.TastyToast

class UserOrderFragment : Fragment(),SearchView.OnQueryTextListener {

    var   userOrderAdapter : UserOrderAdapter? = null
    lateinit var userOrderViewModel: UserOrderViewModel
    lateinit var deleteOrderViewModel: DeleteOrderViewModel
    lateinit var deliveryViewModel: DeliveryViewModel

    var ordertList: MutableList<OrdersUser> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_user_order, container, false)

        val  rsSection: RecyclerView = view.findViewById(R.id.user_order)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)

        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()

        deleteOrderViewModel = ViewModelProvider(activity!!).get(DeleteOrderViewModel::class.java)
        deliveryViewModel = ViewModelProvider(activity!!).get(DeliveryViewModel::class.java)
        userOrderViewModel = ViewModelProvider(activity!!).get(UserOrderViewModel::class.java)
        val user = UserOrderFragmentArgs.fromBundle(arguments!!)

        userOrderViewModel.setEmail(user.mail)


        userOrderViewModel.getUserOrders()
        userOrderViewModel.userOrders.observe(viewLifecycleOwner, Observer { list ->
            ordertList = list.ordersUser
            userOrderAdapter = UserOrderAdapter(

                UserOrderAdapter.DeleteOnClickListener {
                    if (NetworkUtils().isInternetAvailable(activity!!)){
                        dialog.show()
                         deleteOrderViewModel.deleteOrders(list.ordersUser[it].productNumber)
                        list.ordersUser.removeAt(it)
                        userOrderAdapter?.notifyDataSetChanged()

                    }
                    else{
                        TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                    }
                },
                UserOrderAdapter.DeliveryOnClickListener { productUserEmail: String, productNumber: String->
                    if (NetworkUtils().isInternetAvailable(activity!!)){
                        dialog.show()
                         deliveryViewModel.uploadProduct(productUserEmail,productNumber)
                    }
                    else{
                        TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                    }
                },
                context,
                list.ordersUser)

                       rsSection.adapter = userOrderAdapter
                       spinKitView.visibility = View.GONE

            activity!!.viewModelStore.clear()

        })

        userOrderViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()


        })
        deliveryViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            activity!!.viewModelStore.clear()
            dialog.dismiss()

        })

        deliveryViewModel.response.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,  it.status, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            activity!!.viewModelStore.clear()
            dialog.dismiss()

        })

        userOrderViewModel.emptyList.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.CONFUSING).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()

        })

        deleteOrderViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()
            dialog.dismiss()


        })
        deleteOrderViewModel.response.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it.status,TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()
            dialog.dismiss()


        })

        setHasOptionsMenu(true)

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {



        val userinput = newText!!.toLowerCase()
        val newlist: MutableList<OrdersUser> = mutableListOf()
        for (search in ordertList) {
            if (search.productNumber.contains(userinput)) {
                newlist.add(search)
            }
        }
        userOrderAdapter?.search(newlist)
        return true
    }

}