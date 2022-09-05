package bego.market.waffar.ui.fragments.admin.order

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.adapters.UsersOrdersAdapter
import bego.market.waffar.models.UsersOrder
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UsersOrderFragment : Fragment(),SearchView.OnQueryTextListener, MenuProvider {

    @Inject
    lateinit var usersOrdersAdapter : UsersOrdersAdapter
    private val orderViewModel: OrderViewModel by viewModels()
    private var orderList: MutableList<UsersOrder> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_users_orders, container, false)

        val  recyclerViewUsersOrders: RecyclerView = view.findViewById(R.id.users_orders)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar3)
        val buttonLoading: Button = view.findViewById(R.id.reLoading2)
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.dialogTitle).text = "جاري الحذف...."
        dialog.findViewById<TextView>(R.id.txtClose).setOnClickListener { dialog.dismiss() }

        buttonLoading.setOnClickListener {
            orderViewModel.restVariables()
            orderViewModel.getUsersOrders()
        }

        orderViewModel.getUsersOrders()
        orderViewModel.responseForUsersOrder.observe(viewLifecycleOwner) { orders ->
            usersOrdersAdapter.submitList(orders)
            orderList = orders

            usersOrdersAdapter.deleteButtonClick = { orderPosition ->
                if (NetworkUtils().isInternetAvailable(activity!!)) {
                    dialog.show()
                    orderViewModel.deleteUserOrders(orders[orderPosition].mail)
                    orders.removeAt(orderPosition)
                    usersOrdersAdapter.notifyDataSetChanged()
                } else {
                    TastyToast.makeText(
                        activity,
                        "تاكد من اتصالك بالانترنت",
                        TastyToast.STYLE_ALERT
                    ).show()
                }
            }

            recyclerViewUsersOrders.adapter = usersOrdersAdapter
            progressBar.visibility = View.GONE

        }

        usersOrdersAdapter.orderClick = { mail: String, address: String, phone: String, name: String ->
            if (NetworkUtils().isInternetAvailable(activity!!)) {
                findNavController().navigate(
                    UsersOrderFragmentDirections.actionUsersOrdersFragmentToUserOrderFragment2(
                        mail,
                        address,
                        phone,
                        name
                    )
                )

            } else {
                TastyToast.makeText(
                    activity,
                    "تاكد من اتصالك بالانترنت",
                    TastyToast.STYLE_ALERT
                ).show()
            }
        }


        // respond for getUsersOrders
        orderViewModel.connectionErrorForUsersOrder.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ اثناء تحميل المنتجات" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                    progressBar.visibility = View.GONE
                    buttonLoading.visibility = View.VISIBLE
                }
                "تم بنجاح" -> {
                    progressBar.visibility = View.GONE
                    buttonLoading.visibility = View.GONE
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    buttonLoading.visibility = View.GONE
                }
            }
        }

//        orderViewModel.emptyListForUsersOrder.observe(viewLifecycleOwner) {
//            TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
//            spinKitView.visibility = View.GONE
//            activity!!.viewModelStore.clear()
//
//        }

        // respond for deleteUserOrders
        orderViewModel.connectionErrorForDeleteUserOrders.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                progressBar.visibility = View.GONE
                dialog.dismiss()
            }
            orderViewModel.restVariables()
        }

        orderViewModel.responseForDeleteUserOrders.observe(viewLifecycleOwner) {
            if (it.status.isNotEmpty()) {
                TastyToast.makeText(activity, it.status, TastyToast.STYLE_CONFIRM).show()
                progressBar.visibility = View.GONE
                dialog.dismiss()
            }
            orderViewModel.restVariables()

        }


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return view
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        val userInput = newText!!.lowercase(Locale.getDefault())
        val newList: MutableList<UsersOrder> = mutableListOf()
        for (search in orderList) {
            if (search.mail.contains(userInput)) {
                newList.add(search)
            }
        }
        usersOrdersAdapter.search(newList)
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

}