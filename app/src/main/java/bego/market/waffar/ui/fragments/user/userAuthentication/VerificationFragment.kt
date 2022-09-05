package bego.market.waffar.ui.fragments.user.userAuthentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.utils.UserValidation
import bego.market.waffar.ui.activities.HomePage
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class VerificationFragment : Fragment() {

    private lateinit var editTextCode: EditText
    private lateinit var checkCode: Button
    private val verifiedViewModel: VerifiedViewModel by viewModels()
    @Inject
    lateinit var userValidation: UserValidation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
         val view : View = inflater.inflate(R.layout.fragment_verification, container, false)

        editTextCode = view.findViewById(R.id.editTextCode)
        checkCode = view.findViewById(R.id.codeLoginButton)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarLoginVerification)


        verifiedViewModel.connectionError.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                    progressBar.visibility = View.GONE

                }
            }
        }

        verifiedViewModel.serverResponse.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
            when (it) {
                "تأكد من الكود" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                }
                "برجاء اعد المحاوله" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                }
                "تم التحقق بنجاح" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_MESSAGE).show()
                    userValidation.writeLoginStatus(true)
                    startActivity(Intent(activity!!, HomePage::class.java))
                    activity!!.finish()
                }
            }
        }


        checkCode.setOnClickListener {

            if (NetworkUtils().isInternetAvailable(activity!!)){
                if ( editTextCode.text.isEmpty() ){
                    TastyToast.makeText(activity,"تأكد من البيانات",  TastyToast.STYLE_ALERT).show()
                }
                else{
                    progressBar.visibility = View.VISIBLE
                    verifiedViewModel.codeVerified(userValidation.readEmail(),editTextCode.text.toString())

                }
            }
            else{
                TastyToast.makeText(activity,  "تاكد من اتصالك بالانترنت", TastyToast.STYLE_ALERT).show()
            }
        }

        activity!!.onBackPressedDispatcher.addCallback(activity!!, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(VerificationFragmentDirections.actionVerificationFragmentToLoginFragment())

            }
        })

        return view
    }


}