package bego.market.waffar.ui.fragments.admin.product

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.adapters.AdminProductAdapter
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.models.AllProduct
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AdminAllProductFragment : Fragment(),SearchView.OnQueryTextListener, MenuProvider {

    @Inject
    lateinit var adminProductAdapter : AdminProductAdapter
    private val adminAllProductViewModel: AdminAllProductViewModel by viewModels()
    private var productList: MutableList<AllProduct> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View{
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_all_product_admin, container, false)

        val  recyclerViewProductList: RecyclerView = view.findViewById(R.id.adminProduct)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar5)
        val buttonLoading: Button = view.findViewById(R.id.reLoading4)

        buttonLoading.setOnClickListener {
            adminAllProductViewModel.restVariables()
            adminAllProductViewModel.getProducts()
        }

        adminAllProductViewModel.getProducts()
        adminAllProductViewModel.products.observe(viewLifecycleOwner) { products ->
            productList = products
            adminProductAdapter.submitList(products)
            adminProductAdapter.onProductClick = { product ->
                if (NetworkUtils().isInternetAvailable(activity!!)) {
                    findNavController().navigate(
                        AdminAllProductFragmentDirections.actionAdminAllProductFragmentToEditProductFragment(
                            product.id,
                            product.name,
                            product.image,
                            product.description,
                            product.price,
                            product.productOfferPercentage,
                            product.productOfferPrice,
                            product.section
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



            recyclerViewProductList.adapter = adminProductAdapter
        }


        adminAllProductViewModel.connectionError.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ اثناء التحميل" -> {
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

//        adminAllProductViewModel.emptyList.observe(viewLifecycleOwner) {
//            TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
//            spinKitView.visibility = View.GONE
//            activity!!.viewModelStore.clear()
//
//        }


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return view
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        val userInput = newText!!.lowercase(Locale.getDefault())
        val newList: MutableList<AllProduct> = mutableListOf()
        for (search in productList) {
            if (search.name.contains(userInput)) {
                newList.add(search)
            }
        }
        adminProductAdapter.search(newList)
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