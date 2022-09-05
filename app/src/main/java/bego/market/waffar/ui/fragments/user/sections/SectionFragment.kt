package bego.market.waffar.ui.fragments.user.sections

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.adapters.ProductsSectionAdapter
import bego.market.waffar.models.AllProduct
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.data.localDataBase.Cart
import bego.market.waffar.ui.fragments.user.cart.CartViewModel
import com.github.ybq.android.spinkit.SpinKitView
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class SectionFragment : Fragment(),SearchView.OnQueryTextListener, MenuProvider {

    @Inject
    lateinit var productsSectionAdapter : ProductsSectionAdapter
    @Inject
    lateinit var   cart : Cart
    private val sectionViewModel: SectionViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private var productList: MutableList<AllProduct> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_section, container, false)

        val  productsRecyclerView: RecyclerView = view.findViewById(R.id.sectionItem)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)

        val buttonLoading: Button = view.findViewById(R.id.reLoadingGetProductsSection)
        val section = SectionFragmentArgs.fromBundle(arguments!!)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_action_bar, section.name)

        sectionViewModel.getProductsSection(section.name)

        sectionViewModel.products.observe(viewLifecycleOwner) { products ->

            productList = products
            productsSectionAdapter.submitList(products)

            productsRecyclerView.adapter = productsSectionAdapter
            spinKitView.visibility = View.GONE

        }
        productsSectionAdapter.onCartButtonClick = {
            cart.productId = it.id
            cart.productName = it.name
            cart.productImage = it.image
            if (it.productOfferPrice == "0")
                cart.totalPrice = it.price
              else
                cart.totalPrice = it.productOfferPrice


            cart.productQuantity = "1"
             cartViewModel.setProductInCart(cart)

            TastyToast.makeText(activity, "تم الاضافة الى العربة", TastyToast.STYLE_MESSAGE).show()
        }

        productsSectionAdapter.onProductClick = {
            if (NetworkUtils().isInternetAvailable(activity!!)) {
                findNavController().navigate(
                    SectionFragmentDirections.actionSectionFragmentToDetailsFragment(
                        it
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


        sectionViewModel.connectionError.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ اثناء التحميل" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                    spinKitView.visibility = View.GONE
                    buttonLoading.visibility = View.VISIBLE
                }
                "تم بنجاح" -> {
                    spinKitView.visibility = View.GONE
                    buttonLoading.visibility = View.GONE
                }
                else -> {
                    spinKitView.visibility = View.VISIBLE
                    buttonLoading.visibility = View.GONE
                }
            }
        }

//        sectionViewModel.emptyList.observe(viewLifecycleOwner) {
//            TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
//            spinKitView.visibility = View.GONE
//            buttonLoading.visibility = View.VISIBLE
//            productsRecyclerView.visibility = View.GONE
//            activity!!.viewModelStore.clear()
//
//        }



        buttonLoading.setOnClickListener{
            spinKitView.visibility = View.VISIBLE
            buttonLoading.visibility = View.GONE
            sectionViewModel.restVariables()
            sectionViewModel.getProductsSection(section.name)
         }


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)



        return view
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
        productsSectionAdapter.search(newList)
        return true
    }




}