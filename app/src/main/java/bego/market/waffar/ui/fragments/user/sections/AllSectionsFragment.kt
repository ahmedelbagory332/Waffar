package bego.market.waffar.ui.fragments.user.sections

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
import bego.market.waffar.adapters.SectionsAdapter
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.models.Section
import bego.market.waffar.ui.fragments.user.homePage.HomePageViewModel
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AllSectionsFragment : Fragment(),SearchView.OnQueryTextListener, MenuProvider {


    var sections: MutableList<Section> = mutableListOf()
    private val homePageViewModel: HomePageViewModel by viewModels()
    @Inject
    lateinit var sectionsAdapter: SectionsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_all_sections, container, false)

        val sectionsListRecyclerView: RecyclerView = view.findViewById(R.id.sectionItem)
        val buttonLoading: Button = view.findViewById(R.id.reLoadingSections)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar2)

        homePageViewModel.getSections()
        homePageViewModel.sections.observe(viewLifecycleOwner) { sectionsList ->
            sections = sectionsList
            sectionsAdapter.submitList(sectionsList)
            sectionsAdapter.onSectionClick = { section ->
                if (NetworkUtils().isInternetAvailable(activity!!)) {
                    findNavController().navigate(
                        AllSectionsFragmentDirections.actionSectionsFragmentToSectionFragment(section.name)
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
            activity!!.viewModelStore.clear()
        }

        homePageViewModel.connectionError.observe(viewLifecycleOwner) {
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


        buttonLoading.setOnClickListener {
            sectionsListRecyclerView.visibility = View.VISIBLE
            buttonLoading.visibility = View.GONE
            homePageViewModel.restVariables()
            homePageViewModel.getSections()
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {



        val userInput = newText!!.lowercase(Locale.getDefault())
        val newList: MutableList<Section> = mutableListOf()
        for (search in sections) {
            if (search.name.contains(userInput)) {
                newList.add(search)
            }
        }
        sectionsAdapter.search(newList)
        return true
    }



    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }


}