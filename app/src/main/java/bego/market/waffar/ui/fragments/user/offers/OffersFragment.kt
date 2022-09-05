package bego.market.waffar.ui.fragments.user.offers

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
import bego.market.waffar.adapters.OffersAdapter
import bego.market.waffar.models.Offer
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
class OffersFragment : Fragment(),SearchView.OnQueryTextListener, MenuProvider {

    @Inject
    lateinit var offersAdapter : OffersAdapter
    @Inject
    lateinit var   cart : Cart
    private val offerViewModel: OfferViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private var offersList: MutableList<Offer> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_offers, container, false)
        activity!!.viewModelStore.clear()


        val  recyclerViewProductOffers: RecyclerView = view.findViewById(R.id.offerItem)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val buttonLoading: Button = view.findViewById(R.id.reLoading)
        val emptyTv: TextView = view.findViewById(R.id.tvEmpty)




        offerViewModel.getOffers()
        offerViewModel.offers.observe(viewLifecycleOwner) { offers ->
            offersList = offers
            offersAdapter.submitList(offers)

            recyclerViewProductOffers.adapter = offersAdapter
            activity!!.viewModelStore.clear()
        }

        offersAdapter.onCartButtonClick = { offer ->
            cart.productId = offer.id
            cart.productName = offer.name
            cart.productImage = offer.image
            if (offer.productOfferPrice.isEmpty())
                cart.totalPrice = offer.price
            else
                cart.totalPrice = offer.productOfferPrice

            cart.productQuantity = "1"
            cartViewModel.setProductInCart(cart)
            TastyToast.makeText(activity, "تم الاضافة الى العربة", TastyToast.STYLE_MESSAGE)
                .show()

        }

        offersAdapter.onProductClick = { productOfferId->
            if (NetworkUtils().isInternetAvailable(activity!!)) {
                findNavController().navigate(
                    OffersFragmentDirections.actionOffersFragmentToOffersDetailsFragment(productOfferId)
                )
            } else {
                TastyToast.makeText(
                    activity,
                    "تاكد من اتصالك بالانترنت",
                    TastyToast.STYLE_ALERT
                ).show()
            }
        }

        buttonLoading.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            buttonLoading.visibility = View.GONE
            emptyTv.visibility = View.GONE
            offerViewModel.restVariables()
            offerViewModel.getOffers()
        }

        offerViewModel.connectionError.observe(viewLifecycleOwner) {
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

        offerViewModel.emptyList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                progressBar.visibility = View.GONE
                buttonLoading.visibility = View.VISIBLE
                emptyTv.visibility = View.VISIBLE
            }

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
        val newList: MutableList<Offer> = mutableListOf()
        for (search in offersList) {
            if (search.name.contains(userInput)) {
                newList.add(search)
            }
        }
        offersAdapter.search(newList)
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