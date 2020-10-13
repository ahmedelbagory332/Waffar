package bego.market.belbies.Fragments

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
import bego.market.belbies.R
import bego.market.belbies.UserValidation
import bego.market.belbies.ViewModel.CartDatabase
import bego.market.belbies.ViewModel.CartViewModel
import bego.market.belbies.ViewModel.ViewModelFactory.CartViewModelFactory
import com.sdsmdg.tastytoast.TastyToast


class AddressFragment : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_address, container, false)

        val userValidation = UserValidation(activity!!)


       val addressEditText:EditText = view.findViewById(R.id._address)
       val phoneEditText:EditText = view.findViewById(R.id._phone)
        val saveButton:Button = view.findViewById(R.id._save)

        saveButton.setOnClickListener {
             userValidation.writeAddress( addressEditText.text.toString())
            userValidation.writePhone( phoneEditText.text.toString())
            TastyToast.makeText(context,  "تم تحديث البيانات", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
        }


        addressEditText.setText(userValidation.readAddress())
        phoneEditText.setText(userValidation.readPhone())



        return view
    }



}