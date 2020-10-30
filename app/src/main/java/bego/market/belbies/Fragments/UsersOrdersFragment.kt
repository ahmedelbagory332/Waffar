package bego.market.belbies.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.UsersOrdersAdapter
import bego.market.belbies.Models.AllUsersOrders
import bego.market.belbies.Models.UsersOrder
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.ViewModel.*
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.github.ybq.android.spinkit.SpinKitView
import com.sdsmdg.tastytoast.TastyToast

class UsersOrdersFragment : Fragment(),SearchView.OnQueryTextListener {

    var   usersOrdersAdapter : UsersOrdersAdapter? = null
    lateinit var usersOrdersViewModel: UsersOrdersViewModel
    lateinit var deleteUserOrderViewModel: DeleteUserOrderViewModel
    var ordertList: MutableList<UsersOrder> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_users_orders, container, false)

        val  rsSection: RecyclerView = view.findViewById(R.id.users_orders)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)

        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()

        usersOrdersViewModel = ViewModelProvider(activity!!).get(UsersOrdersViewModel::class.java)
        deleteUserOrderViewModel = ViewModelProvider(activity!!).get(DeleteUserOrderViewModel::class.java)



        usersOrdersViewModel.getUsersOrders()
        usersOrdersViewModel.allUsersOrders.observe(viewLifecycleOwner, Observer { list ->
            ordertList = list.UsersOrders
            usersOrdersAdapter =UsersOrdersAdapter(
                               context,
                list.UsersOrders,
                UsersOrdersAdapter.OnClickListener { mail: String, address: String,phone: String, name: String ->
                    if (NetworkUtils().isInternetAvailable(activity!!)){
                        findNavController().navigate(UsersOrdersFragmentDirections.actionUsersOrdersFragmentToUserOrderFragment2(mail,address,phone,name))

                    }
                    else{
                        TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                    }
                },
                UsersOrdersAdapter.DeleteOnClickListener {
                    if (NetworkUtils().isInternetAvailable(activity!!)){
                        dialog.show()
                        deleteUserOrderViewModel.deleteUserOrders(list.UsersOrders[it].mail)

                        list.UsersOrders.removeAt(it)
                        usersOrdersAdapter?.notifyDataSetChanged()
                    }
                    else{
                        TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                    }

                })

                       rsSection.adapter = usersOrdersAdapter
                       spinKitView.visibility = View.GONE

            activity!!.viewModelStore.clear()

        })

        usersOrdersViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()


        })

        usersOrdersViewModel.emptyList.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.CONFUSING).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()

        })

        deleteUserOrderViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()
            dialog.dismiss()


        })
        deleteUserOrderViewModel.response.observe(viewLifecycleOwner, Observer {
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
        val newlist: MutableList<UsersOrder> = mutableListOf()
        for (search in ordertList) {
            if (search.mail.contains(userinput)) {
                newlist.add(search)
            }
        }
        usersOrdersAdapter?.search(newlist)
        return true
    }

}