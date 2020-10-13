package bego.market.belbies.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.CartAdapter
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.UserValidation
import bego.market.belbies.ViewModel.AddReportViewModel
import bego.market.belbies.ViewModel.CartDatabase
import bego.market.belbies.ViewModel.CartViewModel
import bego.market.belbies.ViewModel.SectionViewModel
import bego.market.belbies.ViewModel.ViewModelFactory.CartViewModelFactory
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.sdsmdg.tastytoast.TastyToast


class ReportFragment : Fragment(){

    lateinit var addReportViewModel: AddReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_report, container, false)

        val userValidation = UserValidation(activity!!)

       val reportEditText:EditText = view.findViewById(R.id._reportText)
        val sendButton:Button = view.findViewById(R.id._sendReport)
        addReportViewModel = ViewModelProvider(activity!!).get(AddReportViewModel::class.java)
        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()
        sendButton.setOnClickListener {

             if (NetworkUtils().isInternetAvailable(activity!!)){
                 if (userValidation.readPhone().isEmpty()){
                     TastyToast.makeText(context,  "تاكد اولا من اكمال بياناتك من خيار العنوان", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()

                 }
                 else{
                        if (reportEditText.text.isEmpty()){
                            TastyToast.makeText(context,  "من فضلك اكتب الشكوى او التقرير", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                        }
                     else{
                            dialog.show()
                            addReportViewModel.uploadReport(userValidation.readPhone(),reportEditText.text.toString())
                        }
                 }
             }
             else{
                 TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
             }
         }

        addReportViewModel.response.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            TastyToast.makeText(context,  it.message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
        })

        addReportViewModel.connectionErrorUploadSection.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            TastyToast.makeText(context,  it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
        })

        return view
    }



}