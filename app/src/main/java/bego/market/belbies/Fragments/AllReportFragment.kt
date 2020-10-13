package bego.market.belbies.Fragments

import android.graphics.Color
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
import bego.market.belbies.Adapters.ReportAdapter
import bego.market.belbies.Models.Sections
import bego.market.belbies.NetworkUtils

import bego.market.belbies.R
import bego.market.belbies.ViewModel.*
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.github.ybq.android.spinkit.SpinKitView
import com.sdsmdg.tastytoast.TastyToast

class AllReportFragment : Fragment(){


    lateinit var addReportViewModel: AddReportViewModel
    lateinit var reportAdapter : ReportAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_all_report, container, false)

        val  rsSection: RecyclerView = view.findViewById(R.id.sectionItem)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)
        addReportViewModel = ViewModelProvider(activity!!).get(AddReportViewModel::class.java)


        val buttonLoading: Button = view.findViewById(R.id.reLoadingGetProductsSection)
        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()

        addReportViewModel.getReports()
        addReportViewModel.reportResponse.observe(viewLifecycleOwner, Observer { list ->
            reportAdapter =
                ReportAdapter(
                    activity!!,
                    list.reports,
                    ReportAdapter.DeleteOnClickListener {

                        if (NetworkUtils().isInternetAvailable(activity!!)){
                            dialog.show()
                            addReportViewModel.deleteReports(it.id)
                            list.reports.remove(it)
                            reportAdapter.notifyDataSetChanged()

                        }
                        else{
                            TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                        }
                    })
            rsSection.adapter = reportAdapter
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()
        })

        addReportViewModel.connectionErrorUploadSection.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            rsSection.visibility = View.GONE
            buttonLoading.visibility = View.VISIBLE
            activity!!.viewModelStore.clear()
        })

        addReportViewModel.emptyList.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            rsSection.visibility = View.GONE
            buttonLoading.visibility = View.VISIBLE
            activity!!.viewModelStore.clear()
        })

        addReportViewModel.status.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            dialog.dismiss()
        })

        buttonLoading.setOnClickListener {
            spinKitView.visibility = View.VISIBLE
            rsSection.visibility = View.VISIBLE
            buttonLoading.visibility = View.GONE
            addReportViewModel.getReports()
        }


        return view
    }




}