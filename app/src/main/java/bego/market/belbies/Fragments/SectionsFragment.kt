package bego.market.belbies.Fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.ChooseSectionsAdapter
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.ViewModel.*
import bego.market.belbies.ViewModel.ViewModelFactory.CartViewModelFactory
import com.denzcoskun.imageslider.ImageSlider
import com.github.ybq.android.spinkit.SpinKitView
import com.sdsmdg.tastytoast.TastyToast


class SectionsFragment : Fragment() {


    lateinit var sectionsViewModel: SectionsViewModel
    lateinit var cartViewModel: CartViewModel
    var textCartItemCount: TextView? = null
    lateinit var reLoadingLinearLayout: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_sections, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = CartDatabase.getInstance(application).cartDatabaseDao
        val viewModelFactory = CartViewModelFactory(dataSource, application)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)

        cartViewModel = ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)
        sectionsViewModel = ViewModelProvider(activity!!).get(SectionsViewModel::class.java)

        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider) // init imageSlider
        val  rsSection: RecyclerView = view.findViewById(R.id.sectionsList)
        reLoadingLinearLayout = view.findViewById(R.id._reloadingLinear)
        val buttonLoading: Button = view.findViewById(R.id.reLoading)

        sectionsViewModel.setUpSlider()
        sectionsViewModel.getSections()

        sectionsViewModel.imageList.observe(viewLifecycleOwner, Observer {
            imageSlider.setImageList(it)
            imageSlider.startSliding(3000) // with new period
        })


        sectionsViewModel.sections.observe(viewLifecycleOwner, Observer {
            val sectionsAdapter : ChooseSectionsAdapter =
                ChooseSectionsAdapter(
                    activity!!,
                    it.sections,
                    ChooseSectionsAdapter.OnClickListener {
                        if (NetworkUtils().isInternetAvailable(activity!!)){
                            findNavController().navigate(SectionsFragmentDirections.actionSectionsFragmentToSectionFragment(it))
                        }
                        else{
                            TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                        }
                    })
            rsSection.adapter = sectionsAdapter
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()
        })

        sectionsViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            reLoadingLinearLayout.visibility = View.VISIBLE
            activity!!.viewModelStore.clear()
        })

        buttonLoading.setOnClickListener {
            spinKitView.visibility = View.VISIBLE
            reLoadingLinearLayout.visibility = View.GONE
            sectionsViewModel.getSections()
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.cart, menu)
        val menuItem = menu.findItem(R.id.cartFragment)

        val actionView: View = menuItem.actionView
        textCartItemCount = actionView.findViewById(R.id.cart_badge)

        cartViewModel.cartProducts.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                if (textCartItemCount!!.visibility != View.GONE) {
                    textCartItemCount!!.visibility = View.GONE
                }
            } else {
                textCartItemCount!!.text = it.size.coerceAtMost(99).toString()
                if (textCartItemCount!!.visibility != View.VISIBLE) {
                    textCartItemCount!!.visibility = View.VISIBLE
                }
            }
        })

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }
     }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }



}