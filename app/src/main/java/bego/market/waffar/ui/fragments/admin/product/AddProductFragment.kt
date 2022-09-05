package bego.market.waffar.ui.fragments.admin.product

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bego.market.waffar.utils.NetworkUtils
import bego.market.waffar.ui.fragments.admin.section.AddSectionViewModel
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import bego.market.waffar.R
import java.text.NumberFormat
import java.util.*


@AndroidEntryPoint
class AddProductFragment : Fragment(){

    private val addSectionViewModel: AddSectionViewModel by viewModels()
    private val addProductViewModel: AddProductViewModel by viewModels()

    lateinit var sectionName : String
    lateinit var  adapter : ArrayAdapter<String>
    private var sectionsName : MutableList<String> = mutableListOf()
    private lateinit var  addSectionImage:ImageView
    private lateinit var   selectedImage:Uri
    private var PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_add_product, container, false)

        val spinner: Spinner =  view.findViewById(R.id.spinnerSections)
        val productNameEditText:EditText = view.findViewById(R.id.productNameEditText)
        val productDescriptionEditText:EditText = view.findViewById(R.id.productDescriptionEditText)
        val productPriceEditText:EditText = view.findViewById(R.id.productPriceEditText)
        val productOfferPercentageEditText:EditText = view.findViewById(R.id.productOfferPercentageEditText)
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.dialogTitle).text = "جاري إضافه المنتج...."
        dialog.findViewById<TextView>(R.id.txtClose).setOnClickListener { dialog.dismiss() }
        addSectionImage = view.findViewById(R.id.productImage)

        val addButton:Button = view.findViewById(R.id.addProduct)


        val permReqLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    selectImage()
                }else{
                    TastyToast.makeText(activity, "No Permission Granted", TastyToast.STYLE_ALERT).show()

                }
            }


        addSectionViewModel.getSections()
        addSectionViewModel.sections.observe(viewLifecycleOwner) { sections ->
            sectionsName.clear()
            for (section in sections) {
                sectionsName.add(section.name)
            }
            adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item, sectionsName)
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



        addProductViewModel.response.observe(viewLifecycleOwner) {
            if (it.message.isNotEmpty()) {
                TastyToast.makeText(activity, it.message, TastyToast.STYLE_MESSAGE).show()
                dialog.dismiss()
            }
            addProductViewModel.restAddProductVariables()

        }

        addProductViewModel.connectionError.observe(viewLifecycleOwner) {
              if (it.isNotEmpty()) {
                  TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                  dialog.dismiss()
              }
            addProductViewModel.restAddProductVariables()
        }


        addSectionImage.setOnClickListener {
            if (hasPermissions(activity!!, PERMISSIONS)) {

                selectImage()

            }else {
                permReqLauncher.launch(PERMISSIONS)
            }
        }
        addButton.setOnClickListener {
            if (NetworkUtils().isInternetAvailable(activity!!)){
                if (productNameEditText.text.isEmpty() ||productDescriptionEditText.text.isEmpty() ||productPriceEditText.text.isEmpty() || selectedImage.path.isNullOrEmpty() )
                {
                    TastyToast.makeText(activity,"تاكد من البيانات",TastyToast.STYLE_ALERT).show()

                }
                else
                {
                    dialog.show()
                     if (productOfferPercentageEditText.text.isEmpty())
                    {
                        addProductViewModel.uploadProduct(productNameEditText.text.toString(),productDescriptionEditText.text.toString(),productPriceEditText.text.toString(),sectionName,"0","0",selectedImage)
                    }
                    else
                    {
                        val fmt: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
                        val productOfferPercentage: Double = fmt.parse(productOfferPercentageEditText.text.toString())!!.toDouble()/100
                        val priceOffer = productPriceEditText.text.toString().toInt() * productOfferPercentage
                        addProductViewModel.uploadProduct(productNameEditText.text.toString(),productDescriptionEditText.text.toString(),productPriceEditText.text.toString(),sectionName,priceOffer.toInt().toString(),productOfferPercentageEditText.text.toString(),selectedImage)

                    }

                }
            }
            else{
                TastyToast.makeText(activity,  "تاكد من اتصالك بالانترنت",TastyToast.STYLE_ALERT).show()
            }

        }



        return view
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Get the Image from data
            selectedImage = result.data!!.data!!

            // for upload multipart image
            addSectionImage.setImageURI(selectedImage)
        }
    }


    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)

    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


}