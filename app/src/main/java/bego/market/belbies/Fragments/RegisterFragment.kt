package bego.market.belbies.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.UserValidation
import bego.market.belbies.ViewModel.SignUpViewModel
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.sdsmdg.tastytoast.TastyToast


class RegisterFragment : Fragment() {

    lateinit var editTextName: EditText
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var addUser: Button
    lateinit var signUpViewModel: SignUpViewModel
    lateinit var userValidation: UserValidation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_register, container, false)

        editTextName = view.findViewById(R.id.user_name)
        editTextEmail = view.findViewById(R.id.user_mail)
        editTextPassword = view.findViewById(R.id.user_password)
        addUser = view.findViewById(R.id.signUpButton)
        userValidation = UserValidation(activity!!)

        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()



        signUpViewModel = ViewModelProvider(activity!!).get(SignUpViewModel::class.java)
        signUpViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            activity!!.viewModelStore.clear()

        })

        signUpViewModel.serverResponse.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            when (it) {
                "هذا البريد مسجل من قبل" -> {
                    TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                }
                "حدث خطأ" -> {
                    TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                }
                "تم إرسال رمز التحقق" -> {
                    TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
                    userValidation.writeEmail(editTextEmail.text.toString())
                    userValidation.writeName(editTextName.text.toString())
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragment2ToVerificationFragment())
                }
            }
            activity!!.viewModelStore.clear()
        })

        addUser.setOnClickListener {

            if (NetworkUtils().isInternetAvailable(activity!!)){
                if ( editTextName.text.isEmpty() || editTextEmail.text.isEmpty() || editTextPassword.text.isEmpty() ){
                    TastyToast.makeText(context,"تأكد من البيانات", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                }
                else{
                    dialog.show()
                    signUpViewModel.addUser(editTextName.text.toString(),editTextEmail.text.toString(),editTextPassword.text.toString())

                }
            }
            else{
                TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }




        }

        return view
    }

}