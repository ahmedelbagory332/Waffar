package bego.market.waffar.ui.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import bego.market.waffar.R
import bego.market.waffar.utils.UserValidation
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AddressFragment : Fragment(){

    @Inject
    lateinit var userValidation: UserValidation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_address, container, false)



       val addressEditText:EditText = view.findViewById(R.id._address)
       val phoneEditText:EditText = view.findViewById(R.id._phone)
        val saveButton:Button = view.findViewById(R.id._save)

        saveButton.setOnClickListener {
            if(addressEditText.text.toString().isEmpty() || phoneEditText.text.toString().isEmpty()){
                TastyToast.makeText(activity,  "تاكد من جميع البيانات",  TastyToast.STYLE_MESSAGE).show()
            }else{
                userValidation.writeAddress( addressEditText.text.toString())
                userValidation.writePhone( phoneEditText.text.toString())
                TastyToast.makeText(activity,  "تم تحديث البيانات",  TastyToast.STYLE_MESSAGE).show()
            }

        }


        addressEditText.setText(userValidation.readAddress())
        phoneEditText.setText(userValidation.readPhone())



        return view
    }



}