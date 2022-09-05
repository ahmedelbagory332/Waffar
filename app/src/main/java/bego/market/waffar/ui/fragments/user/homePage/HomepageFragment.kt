package bego.market.waffar.ui.fragments.user.homePage

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.adapters.SectionsAdapter
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.ui.fragments.user.cart.CartViewModel
import com.denzcoskun.imageslider.ImageSlider
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomepageFragment : Fragment(), MenuProvider {


    private val homePageViewModel: HomePageViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private var textCartItemCount: TextView? = null
    @Inject
    lateinit var sectionsAdapter: SectionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.home_page_fragment, container, false)

        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider) // init imageSlider
        val  sectionsListRecyclerView: RecyclerView = view.findViewById(R.id.sectionsList)
        val buttonLoading: Button = view.findViewById(R.id.reLoading)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        homePageViewModel.setUpSlider()
        homePageViewModel.getSections()

        homePageViewModel.imageList.observe(viewLifecycleOwner) { imageList ->
            imageSlider.setImageList(imageList)
            imageSlider.startSliding(3000) // with new period
        }


        homePageViewModel.sections.observe(viewLifecycleOwner) { sectionsList->
            sectionsAdapter.submitList(sectionsList)
            sectionsAdapter.onSectionClick = {section->
                if (NetworkUtils().isInternetAvailable(activity!!)) {
                    findNavController().navigate(
                        HomepageFragmentDirections.actionSectionsFragmentToSectionFragment(section.name)
                    )
                } else {
                    TastyToast.makeText(
                        activity,
                        "تاكد من اتصالك بالانترنت",
                        TastyToast.STYLE_ALERT
                    ).show()
                }
            }
            sectionsListRecyclerView.adapter = sectionsAdapter
        }


        homePageViewModel.connectionError.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ اثناء التحميل" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                    progressBar.visibility = View.GONE
                    buttonLoading.visibility = View.VISIBLE
                    sectionsListRecyclerView.visibility = View.GONE
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



        buttonLoading.setOnClickListener {
            homePageViewModel.restVariables()
            homePageViewModel.getSections()
        }


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return view
    }



    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.cart, menu)
        val menuItem = menu.findItem(R.id.cartFragment)

        val actionView: View = menuItem.actionView!!
        textCartItemCount = actionView.findViewById(R.id.cart_badge)

        cartViewModel.allProduct.observe(viewLifecycleOwner) {
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
        }

        actionView.setOnClickListener { onMenuItemSelected(menuItem) }
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
    }


}