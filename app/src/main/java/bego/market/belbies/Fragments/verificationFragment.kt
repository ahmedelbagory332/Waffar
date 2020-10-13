package bego.market.belbies.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import bego.market.belbies.Activity.HomePage
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.UserValidation
import bego.market.belbies.ViewModel.VerifiedViewModel
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.sdsmdg.tastytoast.TastyToast


class verificationFragment : Fragment() {

    lateinit var editTextCode: EditText
    lateinit var checkCode: Button
    lateinit var verifiedViewModel: VerifiedViewModel
    lateinit var userValidation: UserValidation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         val view : View = inflater.inflate(R.layout.fragment_verification, container, false)

        editTextCode = view.findViewById(R.id.editTextCode)
        checkCode = view.findViewById(R.id.codeLoginButton)
        userValidation = UserValidation(activity!!)

        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()


        verifiedViewModel = ViewModelProvider(activity!!).get(VerifiedViewModel::class.java)
        verifiedViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            activity!!.viewModelStore.clear()
        })
        verifiedViewModel.serverResponse.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            when (it) {
                "تأكد من الكود" -> {
                    TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                }
                "برجاء اعد المحاوله" -> {
                    TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                }
                else -> {
                    TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
                    userValidation.writeLoginStatus(true)
                    startActivity(Intent(activity!!, HomePage::class.java))
                    activity!!.finish()
                }
            }
            activity!!.viewModelStore.clear()
        })


        checkCode.setOnClickListener {

            if (NetworkUtils().isInternetAvailable(activity!!)){
                if ( editTextCode.text.isEmpty() ){
                    TastyToast.makeText(context,"تأكد من البيانات", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                }
                else{
                    dialog.show()
                    verifiedViewModel.codeVerified(userValidation.readEmail(),editTextCode.text.toString())

                }
            }
            else{
                TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }



        }


        return view
    }


}