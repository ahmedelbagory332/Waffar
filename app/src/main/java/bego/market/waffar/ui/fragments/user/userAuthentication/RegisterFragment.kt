package bego.market.waffar.ui.fragments.user.userAuthentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.utils.UserValidation
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    lateinit var editTextName: EditText
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var addUser: Button
    private val registerViewModel: RegisterViewModel by viewModels()
    @Inject
    lateinit var userValidation: UserValidation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_register, container, false)

        editTextName = view.findViewById(R.id.user_name)
        editTextEmail = view.findViewById(R.id.user_mail)
        editTextPassword = view.findViewById(R.id.user_password)
        addUser = view.findViewById(R.id.signUpButton)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarSignIn)


        registerViewModel.connectionError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                when (it) {
                  "تم بنجاح" -> {
                    progressBar.visibility = View.GONE
                } "حدث خطأ" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                } else -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                    progressBar.visibility = View.GONE
                }
             }
         }
     }

        registerViewModel.serverResponse.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
            when (it) {
                "هذا البريد مسجل من قبل" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                }
                "حدث خطأ" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                }
                "تم إرسال رمز التحقق" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_MESSAGE).show()
                    userValidation.writeEmail(editTextEmail.text.toString())
                    userValidation.writeName(editTextName.text.toString())
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragment2ToVerificationFragment())
                }
            }
        }

        addUser.setOnClickListener {

            if (NetworkUtils().isInternetAvailable(activity!!)){
                if ( editTextName.text.isEmpty() || editTextEmail.text.isEmpty() || editTextPassword.text.isEmpty() ){
                    TastyToast.makeText(activity,"تأكد من البيانات", TastyToast.STYLE_ALERT).show()
                }
                else{
                    progressBar.visibility = View.VISIBLE
                    registerViewModel.addUser(editTextName.text.toString(),editTextEmail.text.toString(),editTextPassword.text.toString())

                }
            }
            else{
                TastyToast.makeText(activity,  "تاكد من اتصالك بالانترنت", TastyToast.STYLE_ALERT).show()
            }




        }

        return view
    }

}