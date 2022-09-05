package bego.market.waffar.ui.fragments.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.utils.UserValidation
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReportFragment : Fragment(){

    private val addReportViewModel: AddReportViewModel by viewModels()
    @Inject
    lateinit var userValidation: UserValidation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_report, container, false)


       val reportEditText:EditText = view.findViewById(R.id._reportText)
        val sendButton:Button = view.findViewById(R.id._sendReport)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarReport)

        sendButton.setOnClickListener {

             if (NetworkUtils().isInternetAvailable(activity!!)){
                 if (userValidation.readPhone().isEmpty()){
                     TastyToast.makeText(activity,  "تاكد اولا من اكمال بياناتك من خيار العنوان",  TastyToast.STYLE_ALERT).show()

                 }
                 else{
                        if (reportEditText.text.isEmpty()){
                            TastyToast.makeText(activity,  "من فضلك اكتب الشكوى او التقرير", TastyToast.STYLE_ALERT).show()
                        }
                     else{
                            progressBar.visibility = View.VISIBLE
                            addReportViewModel.uploadReport(userValidation.readPhone(),reportEditText.text.toString())
                        }
                 }
             }
             else{
                 TastyToast.makeText(activity,  "تاكد من اتصالك بالانترنت", TastyToast.STYLE_ALERT).show()
             }
         }

        addReportViewModel.response.observe(viewLifecycleOwner) {
            if (it.message.isNotEmpty()) {
                progressBar.visibility = View.GONE
                TastyToast.makeText(activity, it.message, TastyToast.STYLE_MESSAGE).show()
            }
            addReportViewModel.restVariables()

        }

        addReportViewModel.connectionError.observe(viewLifecycleOwner) {
             if (it.isNotEmpty()) {
                 progressBar.visibility = View.GONE
                 TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
             }
            addReportViewModel.restVariables()

        }

        return view

        }



}