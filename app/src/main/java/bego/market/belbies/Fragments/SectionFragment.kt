package bego.market.belbies.Fragments

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.ProductsSectionAdapter
import bego.market.belbies.Models.AllProduct
import bego.market.belbies.NetworkUtils

import bego.market.belbies.R
import bego.market.belbies.ViewModel.Cart
import bego.market.belbies.ViewModel.CartDatabase
import bego.market.belbies.ViewModel.CartViewModel
import bego.market.belbies.ViewModel.SectionViewModel
import bego.market.belbies.ViewModel.ViewModelFactory.CartViewModelFactory
import com.github.ybq.android.spinkit.SpinKitView
import com.sdsmdg.tastytoast.TastyToast

class SectionFragment : Fragment(),SearchView.OnQueryTextListener{

    var   sectionsAdapterProducts : ProductsSectionAdapter? = null
    lateinit var sectionViewModel: SectionViewModel
    var productList: MutableList<AllProduct> = mutableListOf()
    lateinit var cartViewModel: CartViewModel
    lateinit var   cart : Cart


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_section, container, false)

        val  rsSection: RecyclerView = view.findViewById(R.id.sectionItem)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)
        val application = requireNotNull(this.activity).application
        val dataSource = CartDatabase.getInstance(application).cartDatabaseDao
        val viewModelFactory = CartViewModelFactory(dataSource, application)
        cart  = Cart()

        cartViewModel = ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)
        sectionViewModel = ViewModelProvider(activity!!).get(SectionViewModel::class.java)


        val buttonLoading: Button = view.findViewById(R.id.reLoadingGetProductsSection)

        val position = SectionFragmentArgs.fromBundle(arguments!!)


        sectionViewModel.getSectionName(position.name).observe(viewLifecycleOwner, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_action_bar,it)
            activity!!.viewModelStore.clear()


        })

        sectionViewModel.getProductsSection()
        sectionViewModel.products.observe(viewLifecycleOwner, Observer { it ->
            productList = it.allProducts
            sectionsAdapterProducts =ProductsSectionAdapter(
                               context,
                              it.allProducts,
                               ProductsSectionAdapter.OnClickListener {
                                  if (NetworkUtils().isInternetAvailable(activity!!)){
                                      findNavController().navigate(SectionFragmentDirections.actionSectionFragmentToDetailsFragment(it))
                                  }
                                   else{
                                      TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                                  }
                               },
                                ProductsSectionAdapter.ButtonOnClickListener{
                                    cart.productId = it.id
                                    cart.productName = it.name
                                    cart.productImage = it.image
                                    if (it.productOfferPrice.isEmpty()) cart.totalPrice = it.price else cart.totalPrice = it.productOfferPrice
                                    cart.productQuantity = "1"
                                    cartViewModel.setProductInCart(cart)
                                    TastyToast.makeText(context,  "تم الاضافة الى العربة", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()

                                })

                       rsSection.adapter = sectionsAdapterProducts
                       spinKitView.visibility = View.GONE

            activity!!.viewModelStore.clear()

        })

        sectionViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            buttonLoading.visibility = View.VISIBLE
            rsSection.visibility = View.GONE
            activity!!.viewModelStore.clear()


        })

        sectionViewModel.emptyList.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.CONFUSING).show()
            spinKitView.visibility = View.GONE
            buttonLoading.visibility = View.VISIBLE
            rsSection.visibility = View.GONE
            activity!!.viewModelStore.clear()

        })



        buttonLoading.setOnClickListener{
            spinKitView.visibility = View.VISIBLE
            rsSection.visibility = View.VISIBLE
            buttonLoading.visibility = View.GONE
            sectionViewModel.getProductsSection()

         }


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
        val newlist: MutableList<AllProduct> = mutableListOf()
        for (search in productList) {
            if (search.name.contains(userinput)) {
                newlist.add(search)
            }
        }
        sectionsAdapterProducts?.search(newlist)
        return true
    }


}