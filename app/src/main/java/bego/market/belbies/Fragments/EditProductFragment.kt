package bego.market.belbies.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.ViewModel.AddSectionViewModel
import bego.market.belbies.ViewModel.EditProductViewModel
import bego.market.belbies.ViewModel.ViewModelFactory.AddSectionModelFactory
import bego.market.belbies.ViewModel.ViewModelFactory.EditProductModelFactory
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.bumptech.glide.Glide
import com.sdsmdg.tastytoast.TastyToast
import java.text.NumberFormat
import java.util.*


@Suppress("UNREACHABLE_CODE", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EditProductFragment : Fragment(){

    lateinit var addSectionViewModel: AddSectionViewModel
    lateinit var editProductViewModel: EditProductViewModel

    lateinit var sectionName : String
    lateinit var  adapter : ArrayAdapter<String>
    var sectionsName : MutableList<String> = mutableListOf()
    lateinit var  editSectionImage:ImageView
    lateinit var ProductNameEditText:EditText
    lateinit var ProductDescriptionEditText:EditText
    lateinit var  ProductPriceEditText:EditText
    lateinit var  ProductOfferPercentageEditText:EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_edit_product, container, false)

        val spinner: Spinner =  view.findViewById(R.id.spinnerSections)
         ProductNameEditText = view.findViewById(R.id.productNameEditText)
         ProductDescriptionEditText = view.findViewById(R.id.productDescriptionEditText)
         ProductPriceEditText = view.findViewById(R.id.productPriceEditText)
         ProductOfferPercentageEditText = view.findViewById(R.id.productOfferPercentageEditText)
        editSectionImage = view.findViewById(R.id.productImage)

        val editButton:Button = view.findViewById(R.id.editProduct)
        val application = requireNotNull(this.activity).application
        val viewModelFactoryAddSection = AddSectionModelFactory(application)
        val viewModelFactoryEditProduct = EditProductModelFactory(application)
        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()
        val product = EditProductFragmentArgs.fromBundle(arguments!!)

        Glide.with(context!!).load(product.image).into(editSectionImage)
        ProductNameEditText.setText(product.name)
        ProductDescriptionEditText.setText(product.description)
        ProductPriceEditText.setText(product.price)
        ProductOfferPercentageEditText.setText(product.productOfferPercentage)

        addSectionViewModel = ViewModelProvider(activity!!,viewModelFactoryAddSection).get(AddSectionViewModel::class.java)
        editProductViewModel = ViewModelProvider(activity!!,viewModelFactoryEditProduct).get(EditProductViewModel::class.java)

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
        editProductViewModel.response.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it.message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            dialog.dismiss()
            adapter.notifyDataSetChanged()
        })
        editProductViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            dialog.dismiss()
         })

        editButton.setOnClickListener {
            if (NetworkUtils().isInternetAvailable(activity!!)){
                dialog.show()
                if (ProductOfferPercentageEditText.text.isEmpty())
                {
                    editProductViewModel.editProduct(product.id.toInt(),ProductNameEditText.text.toString(),ProductDescriptionEditText.text.toString(),ProductPriceEditText.text.toString(),sectionName,"0","0")
                }
                else
                {
                    val fmt: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
                    val productOfferPercentage: Double = fmt.parse(ProductOfferPercentageEditText.text.toString()).toDouble()/100
                    val priceOffer = ProductPriceEditText.text.toString().toInt() * productOfferPercentage
                    editProductViewModel.editProduct(product.id.toInt(),ProductNameEditText.text.toString(),ProductDescriptionEditText.text.toString(),ProductPriceEditText.text.toString(),sectionName,priceOffer.toInt().toString(),ProductOfferPercentageEditText.text.toString())
                }
            }
            else{
                TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }

        }
        return view
    }

    }