package bego.market.waffar.ui.fragments.admin.product

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.R
import bego.market.waffar.ui.fragments.admin.section.AddSectionViewModel
import com.bumptech.glide.Glide
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class EditProductFragment : Fragment(){

    private val addSectionViewModel: AddSectionViewModel by viewModels()
    private val editProductViewModel: EditProductViewModel by viewModels()

    lateinit var sectionName : String
    lateinit var  adapter : ArrayAdapter<String>
    private var sectionsName : MutableList<String> = mutableListOf()
    private lateinit var  editSectionImage:ImageView
    private lateinit var productNameEditText:EditText
    private lateinit var productDescriptionEditText:EditText
    private lateinit var  productPriceEditText:EditText
    private lateinit var  productOfferPercentageEditText:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_edit_product, container, false)

        val spinner: Spinner =  view.findViewById(R.id.spinnerSections)

         productNameEditText = view.findViewById(R.id.productNameEditText)
         productDescriptionEditText = view.findViewById(R.id.productDescriptionEditText)
         productPriceEditText = view.findViewById(R.id.productPriceEditText)
         productOfferPercentageEditText = view.findViewById(R.id.productOfferPercentageEditText)
         editSectionImage = view.findViewById(R.id.productImage)

        val editButton:Button = view.findViewById(R.id.editProduct)
        val product = EditProductFragmentArgs.fromBundle(arguments!!)
        val dialog = Dialog(activity!!)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.dialogTitle).text = "جاري تعديل المنتج...."
        dialog.findViewById<TextView>(R.id.txtClose).setOnClickListener { dialog.dismiss() }

        Glide.with(context!!).load(product.image).into(editSectionImage)
        productNameEditText.setText(product.name)
        productDescriptionEditText.setText(product.description)
        productPriceEditText.setText(product.price)
        productOfferPercentageEditText.setText(product.productOfferPercentage)


        addSectionViewModel.getSections()
        addSectionViewModel.sections.observe(viewLifecycleOwner) { sections ->
            sectionsName.clear()
            for (section in sections) {
                sectionsName.add(section.name)
            }
            adapter =
                ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item, sectionsName)
            // Set the drop down view resource
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            // Finally, data bind the spinner object with adapter
            spinner.adapter = adapter
            // Set an on item selected listener for spinner object
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    // Display the selected item text on text view
                    sectionName = parent.getItemAtPosition(position).toString()


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }
        }


        editProductViewModel.response.observe(viewLifecycleOwner) {
            if (it.message.isNotEmpty()) {
                TastyToast.makeText(activity, it.message, TastyToast.STYLE_MESSAGE).show()
                dialog.dismiss()
            }
            editProductViewModel.restEditProductVariables()
        }

        editProductViewModel.connectionError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                dialog.dismiss()
            }
            editProductViewModel.restEditProductVariables()
        }

        editButton.setOnClickListener {
            if (NetworkUtils().isInternetAvailable(activity!!)){
                if (productNameEditText.text.isEmpty() ||productDescriptionEditText.text.isEmpty() ||productPriceEditText.text.isEmpty() )
                {
                    TastyToast.makeText(activity,"تاكد من البيانات",TastyToast.STYLE_ALERT).show()

                }else{
                    dialog.show()
                    if (productOfferPercentageEditText.text.isEmpty())
                    {
                        editProductViewModel.editProduct(product.id.toInt(),productNameEditText.text.toString(),productDescriptionEditText.text.toString(),productPriceEditText.text.toString(),sectionName,"0","0")
                    }
                    else
                    {
                        val fmt: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
                        val productOfferPercentage: Double = fmt.parse(productOfferPercentageEditText.text.toString())!!.toDouble()/100
                        val priceOffer = productPriceEditText.text.toString().toInt() * productOfferPercentage
                        editProductViewModel.editProduct(product.id.toInt(),productNameEditText.text.toString(),productDescriptionEditText.text.toString(),productPriceEditText.text.toString(),sectionName,priceOffer.toInt().toString(),productOfferPercentageEditText.text.toString())
                    }
                }


            }
            else{
                TastyToast.makeText(activity,  "تاكد من اتصالك بالانترنت", TastyToast.STYLE_ALERT).show()
            }

        }
        return view
    }

    }