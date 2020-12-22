package bego.market.belbies.Fragments

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.AdminProductAdapter
import bego.market.belbies.Models.AllProducts
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.ViewModel.*
import com.github.ybq.android.spinkit.SpinKitView
import com.sdsmdg.tastytoast.TastyToast

class AdminAllProductFragment : Fragment(),SearchView.OnQueryTextListener {

    var   adminProductAdapter : AdminProductAdapter? = null
    lateinit var adminAllProductViewModel: AdminAllProductViewModel
    var productList: MutableList<AllProducts> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_all_product_admin, container, false)

        val  rsSection: RecyclerView = view.findViewById(R.id.adminProduct)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)



        adminAllProductViewModel = ViewModelProvider(activity!!).get(AdminAllProductViewModel::class.java)





        adminAllProductViewModel.getProducts()
        adminAllProductViewModel.products.observe(viewLifecycleOwner, Observer { it ->
            productList = it.allProducts
            adminProductAdapter =AdminProductAdapter(
                               context,
                              it.allProducts,
                          AdminProductAdapter.OnClickListener {
                              if (NetworkUtils().isInternetAvailable(activity!!)){
                                  findNavController().navigate(AdminAllProductFragmentDirections.actionAdminAllProductFragmentToEditProductFragment(it.id,it.name,it.image,it.description,it.price
                                      ,it.productOfferPercentage,it.productOfferPrice,it.section))
                              }
                              else{
                                  TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                              }

                               })

                       rsSection.adapter = adminProductAdapter
                       spinKitView.visibility = View.GONE

            activity!!.viewModelStore.clear()

        })

        adminAllProductViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()


        })

        adminAllProductViewModel.emptyList.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.CONFUSING).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()

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
        val newlist: MutableList<AllProducts> = mutableListOf()
        for (search in productList) {
            if (search.name.contains(userinput)) {
                newlist.add(search)
            }
        }
        adminProductAdapter?.search(newlist)
        return true
    }

}