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
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import bego.market.belbies.Activity.AdminActivity
import bego.market.belbies.Activity.HomePage
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.UserValidation
import bego.market.belbies.ViewModel.LoginViewModel
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.sdsmdg.tastytoast.TastyToast


class LoginFragment : Fragment() {

    lateinit var editTextMail: EditText
    lateinit var editTextPassword: EditText
    lateinit var signIn: Button
    lateinit var loginViewModel: LoginViewModel
    lateinit var userValidation: UserValidation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         val view : View = inflater.inflate(R.layout.fragment_login, container, false)

        val textView:TextView = view.findViewById(R.id.toSignUp)
        editTextMail = view.findViewById(R.id.loginEditTextEmail)
        editTextPassword = view.findViewById(R.id.loginEditTextPassword)
        signIn = view.findViewById(R.id.signInButton)
        userValidation = UserValidation(activity!!)

        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()
        loginViewModel = ViewModelProvider(activity!!).get(LoginViewModel::class.java)
        loginViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            activity!!.viewModelStore.clear()
        })
        loginViewModel.serverResponse.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            when (it) {
                "تأكد من البريد او الرقم السرى" -> {
                    TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                }
                else -> {
                    when (it) {
                        "0" -> {
                            TastyToast.makeText(context,"لم يتم تاكيد البريد الخاص بك برجاء ادخال الكود المرسل لك", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToVerificationFragment())

                        }
                        "100" -> {
                            userValidation.writeLoginStatusAdmin(true)
                            userValidation.writeEmail(editTextMail.text.toString())
                            startActivity(Intent(activity!!, AdminActivity::class.java))
                            activity!!.finish()

                        }
                        else -> {
                            userValidation.writeLoginStatus(true)
                            userValidation.writeEmail(editTextMail.text.toString())
                            startActivity(Intent(activity!!, HomePage::class.java))
                            activity!!.finish()
                        }
                    }

                }
            }
            activity!!.viewModelStore.clear()
        })


        signIn.setOnClickListener {

            if (NetworkUtils().isInternetAvailable(activity!!)){
                if ( editTextMail.text.isEmpty() || editTextPassword.text.isEmpty() ){
                    TastyToast.makeText(context,"تأكد من البيانات", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                }
                else{
                    dialog.show()
                    loginViewModel.loginUser(editTextMail.text.toString(),editTextPassword.text.toString())

                }
            }
            else{
                TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }

        }
        textView.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment2())

        }
      return view
    }


}