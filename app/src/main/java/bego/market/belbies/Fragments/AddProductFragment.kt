package bego.market.belbies.Fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.ViewModel.AddProductViewModel
import bego.market.belbies.ViewModel.AddSectionViewModel
import bego.market.belbies.ViewModel.ViewModelFactory.AddProductModelFactory
import bego.market.belbies.ViewModel.ViewModelFactory.AddSectionModelFactory
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.sdsmdg.tastytoast.TastyToast


class AddProductFragment : Fragment(){

    lateinit var addSectionViewModel: AddSectionViewModel
    lateinit var addProductViewModel: AddProductViewModel

    lateinit var sectionName : String
    lateinit var  adapter : ArrayAdapter<String>
    var sectionsName : MutableList<String> = mutableListOf()
    private val IMAGE = 100
    lateinit var  addSectionImage:ImageView
    lateinit var   selectedImage:Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_add_product, container, false)


        val spinner: Spinner =  view.findViewById(R.id.spinnerSections)
        val ProductNameEditText:EditText = view.findViewById(R.id.productNameEditText)
        val ProductDescriptionEditText:EditText = view.findViewById(R.id.productDescriptionEditText)
        val ProductPriceEditText:EditText = view.findViewById(R.id.productPriceEditText)
        val ProductOfferPriceEditText:EditText = view.findViewById(R.id.productOfferPriceEditText)
        val ProductOfferPercentageEditText:EditText = view.findViewById(R.id.productOfferPercentageEditText)

        addSectionImage = view.findViewById(R.id.productImage)

        val addButton:Button = view.findViewById(R.id.addProduct)

        val application = requireNotNull(this.activity).application
        val viewModelFactoryAddSection = AddSectionModelFactory(application)
        val viewModelFactoryAddProduct = AddProductModelFactory(application)

        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()

        addSectionViewModel = ViewModelProvider(activity!!,viewModelFactoryAddSection).get(AddSectionViewModel::class.java)

        addProductViewModel = ViewModelProvider(activity!!,viewModelFactoryAddProduct).get(AddProductViewModel::class.java)



        addSectionViewModel.getSections()
        addSectionViewModel.sections.observe(viewLifecycleOwner, Observer {
            sectionsName.clear()
            for (names in it.sections)
            {
                sectionsName.add(names.name)
            }
             adapter = ArrayAdapter<String>(
                activity!!, // Context
                android.R.layout.simple_spinner_item, // Layout
                sectionsName// Array
            )
            // Set the drop down view resource
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            // Finally, data bind the spinner object with dapter
            spinner.adapter = adapter

            // Set an on item selected listener for spinner object
            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                    // Display the selected item text on text view
                    sectionName = parent.getItemAtPosition(position).toString()
                 }

                override fun onNothingSelected(parent: AdapterView<*>){
                    // Another interface callback
                }
            }
        })

        addSectionViewModel.response.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it.message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            dialog.dismiss()
            addSectionViewModel.getSections()
            adapter.notifyDataSetChanged()

        })

        addProductViewModel.response.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it.message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            dialog.dismiss()
            adapter.notifyDataSetChanged()
        })
        addProductViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            dialog.dismiss()
            ProductOfferPriceEditText.setText(it)
         })


        addSectionImage.setOnClickListener {
            selectImage()
        }
        addButton.setOnClickListener {
            if (NetworkUtils().isInternetAvailable(activity!!)){
                if (ProductNameEditText.text.isEmpty() ||ProductDescriptionEditText.text.isEmpty() ||ProductPriceEditText.text.isEmpty() || selectedImage.path.isNullOrEmpty() )
                {
                    TastyToast.makeText(context,"تاكد من البيانات", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()

                }
                else
                {
                    dialog.show()
                    if (ProductOfferPriceEditText.text.isEmpty() ||ProductOfferPercentageEditText.text.isEmpty())
                    {
                        addProductViewModel.uploadProduct(ProductNameEditText.text.toString(),ProductDescriptionEditText.text.toString(),ProductPriceEditText.text.toString(),sectionName,"0","0",selectedImage,application)
                    }
                    else
                    {
                        addProductViewModel.uploadProduct(ProductNameEditText.text.toString(),ProductDescriptionEditText.text.toString(),ProductPriceEditText.text.toString(),sectionName,ProductOfferPriceEditText.text.toString(),ProductOfferPercentageEditText.text.toString(),selectedImage,application)

                    }

                }            }
            else{
                TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }

        }



        return view
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE)
    }


     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE && resultCode == RESULT_OK && data != null) {
            // Get the Image from data
             selectedImage = data.data!!

            // for upload multipart image
            addSectionImage.setImageURI(selectedImage)

        }
    }



}