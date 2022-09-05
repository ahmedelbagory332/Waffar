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
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.utils.UserValidation
import bego.market.waffar.ui.activities.AdminActivity
import bego.market.waffar.ui.activities.HomePage
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var editTextMail: EditText
    private lateinit var textView:TextView
    private lateinit var editTextPassword: EditText
    private lateinit var signIn: Button
    private val loginViewModel: LoginViewModel by viewModels()
    @Inject
     lateinit var userValidation: UserValidation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
         val view : View = inflater.inflate(R.layout.fragment_login, container, false)

        textView = view.findViewById(R.id.toSignUp)
        editTextMail = view.findViewById(R.id.loginEditTextEmail)
        editTextPassword = view.findViewById(R.id.loginEditTextPassword)
        signIn = view.findViewById(R.id.signInButton)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarLogin)



        loginViewModel.connectionError.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                    progressBar.visibility = View.GONE
                }"تم بنجاح" -> {
                    progressBar.visibility = View.GONE
                }
            }
        }

        loginViewModel.serverResponse.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
            when (it) {
                "تأكد من البريد او الرقم السرى" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                }
                "0" -> {
                    TastyToast.makeText(
                        activity,
                        "لم يتم تاكيد البريد الخاص بك برجاء ادخال الكود المرسل لك",
                        TastyToast.STYLE_ALERT
                    ).show()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToVerificationFragment())
                }
                "100" -> {
                    // user as admin is log in
                    userValidation.writeLoginStatusAdmin(true)
                    userValidation.writeEmail(editTextMail.text.toString())
                    startActivity(Intent(activity!!, AdminActivity::class.java))
                    activity!!.finish()

                }
                "1" -> {
                    // user is log in
                    userValidation.writeLoginStatus(true)
                    userValidation.writeEmail(editTextMail.text.toString())
                    startActivity(Intent(activity!!, HomePage::class.java))
                    activity!!.finish()
                }

            }
        }


        signIn.setOnClickListener {
            if (NetworkUtils().isInternetAvailable(activity!!)){
                if ( editTextMail.text.isEmpty() || editTextPassword.text.isEmpty() ){
                    TastyToast.makeText(activity,"تأكد من البيانات", TastyToast.STYLE_ALERT).show()
                }
                else{
                    progressBar.visibility = View.VISIBLE
                    loginViewModel.loginUser(editTextMail.text.toString(),editTextPassword.text.toString())
                }
            }
            else{
                TastyToast.makeText(activity,  "تاكد من اتصالك بالانترنت", TastyToast.STYLE_ALERT).show()
            }

        }
        textView.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment2())

        }
      return view
    }


}