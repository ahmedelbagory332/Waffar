package bego.market.waffar.ui.fragments.report

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.adapters.ReportAdapter
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllReportFragment : Fragment(){


    private val addReportViewModel: AddReportViewModel by viewModels()
    @Inject
    lateinit var reportAdapter : ReportAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_all_report, container, false)

        val recyclerViewReportList: RecyclerView = view.findViewById(R.id.sectionItem)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar7)
        val buttonLoading: Button = view.findViewById(R.id.reLoading)

        addReportViewModel.getReports()
        addReportViewModel.reportResponse.observe(viewLifecycleOwner) { reports ->
            reportAdapter.submitList(reports)
            reportAdapter.deleteButtonClick = {
                if (NetworkUtils().isInternetAvailable(activity!!)) {
                    addReportViewModel.deleteReports(it.id)
                    reports.remove(it)
                    reportAdapter.notifyDataSetChanged()

                } else {
                    TastyToast.makeText(
                        activity,
                        "تاكد من اتصالك بالانترنت",
                        TastyToast.STYLE_ALERT
                    ).show()
                }
            }

            recyclerViewReportList.adapter = reportAdapter

        }

        addReportViewModel.connectionError.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ" -> {
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

//        addReportViewModel.emptyList.observe(viewLifecycleOwner) {
//            TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
//            spinKitView.visibility = View.GONE
//            recyclerViewReportList.visibility = View.GONE
//            buttonLoading.visibility = View.VISIBLE
//            activity!!.viewModelStore.clear()
//        }

        addReportViewModel.status.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
            TastyToast.makeText(activity, it, TastyToast.STYLE_MESSAGE).show()

        }

        buttonLoading.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            buttonLoading.visibility = View.GONE
            addReportViewModel.restVariables()
            addReportViewModel.getReports()

        }


        return view
    }




}