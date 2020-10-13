package bego.market.belbies.Fragments

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.ChooseSectionsAdapter
import bego.market.belbies.Models.Sections
import bego.market.belbies.NetworkUtils

import bego.market.belbies.R
import bego.market.belbies.ViewModel.*
import com.github.ybq.android.spinkit.SpinKitView
import com.sdsmdg.tastytoast.TastyToast

class AllSectionFragment : Fragment(),SearchView.OnQueryTextListener{


    var sections: MutableList<Sections> = mutableListOf()
    lateinit var sectionsViewModel: SectionsViewModel
    lateinit var sectionsAdapter : ChooseSectionsAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_all_section, container, false)

        val  rsSection: RecyclerView = view.findViewById(R.id.sectionItem)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)
        sectionsViewModel = ViewModelProvider(activity!!).get(SectionsViewModel::class.java)


        val buttonLoading: Button = view.findViewById(R.id.reLoadingGetProductsSection)

        sectionsViewModel.getSections()
        sectionsViewModel.sections.observe(viewLifecycleOwner, Observer {
            sections = it.sections
            sectionsAdapter =
                ChooseSectionsAdapter(
                    activity!!,
                    it.sections,
                    ChooseSectionsAdapter.OnClickListener {
                        if (NetworkUtils().isInternetAvailable(activity!!)){
                            findNavController().navigate(AllSectionFragmentDirections.actionSectionsFragmentToSectionFragment(it))
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
            rsSection.visibility = View.GONE
            buttonLoading.visibility = View.VISIBLE
            activity!!.viewModelStore.clear()
        })

        buttonLoading.setOnClickListener {
            spinKitView.visibility = View.VISIBLE
            rsSection.visibility = View.VISIBLE
            buttonLoading.visibility = View.GONE
            sectionsViewModel.getSections()
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
        val newlist: MutableList<Sections> = mutableListOf()
        for (search in sections) {
            if (search.name.contains(userinput)) {
                newlist.add(search)
            }
        }
        sectionsAdapter.search(newlist)
        return true
    }


}