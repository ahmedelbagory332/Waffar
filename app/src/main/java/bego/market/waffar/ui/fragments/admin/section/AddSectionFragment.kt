package bego.market.waffar.ui.fragments.admin.section

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
import bego.market.waffar.R
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSectionFragment : Fragment(){

    private val addSectionViewModel: AddSectionViewModel by viewModels()
    private val deleteSectionViewModel: DeleteSectionViewModel by viewModels()
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
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_add_section, container, false)


        val spinner: Spinner =  view.findViewById(R.id.spinner1)
        val addSection:EditText = view.findViewById(R.id.addSectionEditText)
        addSectionImage = view.findViewById(R.id.sectionImage)
        val deleteButton:Button = view.findViewById(R.id.deleteSection)
        val addButton:Button = view.findViewById(R.id.addSection)
         val dialog = Dialog(activity!!)
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
         dialog.setCancelable(false)
         dialog.setContentView(R.layout.dialog)
         dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         dialog.findViewById<TextView>(R.id.dialogTitle).text = "جاري التحميل.."
         dialog.findViewById<TextView>(R.id.txtClose).setOnClickListener { dialog.dismiss() }

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



         deleteSectionViewModel.response.observe(viewLifecycleOwner) {
              if (it.status.isNotEmpty()){
                  TastyToast.makeText(activity, it.status, TastyToast.STYLE_MESSAGE).show()
                  addSectionViewModel.getSections()
                  sectionsName.remove(spinner.selectedItem)
                  adapter.notifyDataSetChanged()
                  dialog.dismiss()
              }
             deleteSectionViewModel.restVariables()

         }

         addSectionViewModel.response.observe(viewLifecycleOwner) {
               if (it.message.isNotEmpty()){
                   TastyToast.makeText(activity, it.message, TastyToast.STYLE_MESSAGE).show()
                   addSectionViewModel.getSections()
                   adapter.notifyDataSetChanged()
                   dialog.dismiss()
               }
             addSectionViewModel.restVariables()
         }

         deleteSectionViewModel.connectionError.observe(viewLifecycleOwner) {
             if (it.isNotEmpty()){
                 TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                 dialog.dismiss()
             }
             deleteSectionViewModel.restVariables()
         }

         addSectionViewModel.connectionError.observe(viewLifecycleOwner) {
             if (it.isNotEmpty()){
                 TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                 dialog.dismiss()
             }
             addSectionViewModel.restVariables()
         }

         deleteButton.setOnClickListener {

            if (NetworkUtils().isInternetAvailable(activity!!)){
                dialog.show()
                deleteSectionViewModel.deleteSection(sectionName)
            }
            else{
                TastyToast.makeText(activity,  "تاكد من اتصالك بالانترنت", TastyToast.STYLE_ALERT).show()
            }

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
                if (addSection.text.isEmpty() || selectedImage.path.isNullOrEmpty() )
                {
                    TastyToast.makeText(activity,"تاكد من البيانات", TastyToast.STYLE_ALERT).show()
                }
                else
                {
                    dialog.show()
                    addSectionViewModel.uploadSections(addSection.text.toString(),selectedImage)
                }

            }
            else{
                TastyToast.makeText(activity,  "تاكد من اتصالك بالانترنت", TastyToast.STYLE_ALERT).show()
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