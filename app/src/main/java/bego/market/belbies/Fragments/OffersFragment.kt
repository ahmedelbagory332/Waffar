package bego.market.belbies.Fragments

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.OffersAdapter
import bego.market.belbies.Models.Offer
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.ViewModel.*
import bego.market.belbies.ViewModel.ViewModelFactory.CartViewModelFactory
import com.github.ybq.android.spinkit.SpinKitView
import com.sdsmdg.tastytoast.TastyToast

class OffersFragment : Fragment(),SearchView.OnQueryTextListener {

    var offersAdapter : OffersAdapter? = null
    lateinit var offerViewModel: OfferViewModel
    var offersList: MutableList<Offer> = mutableListOf()
    lateinit var cartViewModel: CartViewModel
    lateinit var   cart : Cart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_offers, container, false)
        activity!!.viewModelStore.clear()

        offerViewModel = ViewModelProvider(activity!!).get(OfferViewModel::class.java)

        val  rsSection: RecyclerView = view.findViewById(R.id.offerItem)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)

        val application = requireNotNull(this.activity).application
        val dataSource = CartDatabase.getInstance(application).cartDatabaseDao
        val viewModelFactory = CartViewModelFactory(dataSource, application)
        cart  = Cart()

        val buttonLoading: Button = view.findViewById(R.id.reLoading)

        cartViewModel = ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)

        offerViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            buttonLoading.visibility = View.VISIBLE
            rsSection.visibility = View.GONE

            activity!!.viewModelStore.clear()
        })

        offerViewModel.emptyList.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.CONFUSING).show()
            spinKitView.visibility = View.GONE
            buttonLoading.visibility = View.VISIBLE
            rsSection.visibility = View.GONE
            activity!!.viewModelStore.clear()

        })

        offerViewModel.getOffers()
        offerViewModel.offers.observe(viewLifecycleOwner, Observer { it ->
            offersList = it.offers
            offersAdapter =OffersAdapter(
                context,
                it.offers,
                OffersAdapter.OnClickListener {
                    if (NetworkUtils().isInternetAvailable(activity!!)){
                        findNavController().navigate(OffersFragmentDirections.actionOffersFragmentToOffersDetailsFragment(it))
                    }
                    else{
                        TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                    }
                },
                OffersAdapter.ButtonOnClickListener{
                    cart.productId = it.id
                    cart.productName = it.name
                    cart.productImage = it.image
                    if (it.productOfferPrice.isEmpty()) cart.totalPrice = it.price else cart.totalPrice = it.productOfferPrice
                    cart.productQuantity = "1"
                    cartViewModel.setProductInCart(cart)
                    TastyToast.makeText(context,  "تم الاضافة الى العربة", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()

                })
            rsSection.adapter = offersAdapter
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()
        })

        buttonLoading.setOnClickListener {
            spinKitView.visibility = View.VISIBLE
            buttonLoading.visibility = View.GONE
            rsSection.visibility = View.VISIBLE
            offerViewModel.getOffers()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {



        val userinput = newText!!.toLowerCase()
        val newlist: MutableList<Offer> = mutableListOf()
        for (search in offersList) {
            if (search.name.contains(userinput)) {
                newlist.add(search)
            }
        }
        offersAdapter?.search(newlist)
        return true
    }


}