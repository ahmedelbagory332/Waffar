package bego.market.belbies.Fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
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
import bego.market.belbies.NetworkUtils
import bego.market.belbies.R
import bego.market.belbies.ViewModel.AddSectionViewModel
import bego.market.belbies.ViewModel.DeleteSectionViewModel
import bego.market.belbies.ViewModel.ViewModelFactory.AddSectionModelFactory
  import com.sdsmdg.tastytoast.TastyToast
import dmax.dialog.SpotsDialog


class AddSectionFragment : Fragment(){

    lateinit var addSectionViewModel: AddSectionViewModel
    lateinit var deleteSectionViewModel: DeleteSectionViewModel
    lateinit var sectionName : String
    lateinit var  adapter : ArrayAdapter<String>
    var sectionsName : MutableList<String> = mutableListOf()
    private val IMAGE = 100
    lateinit var  addSectionImage:ImageView
    lateinit var   selectedImage:Uri
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_add_section, container, false)


        val spinner: Spinner =  view.findViewById(R.id.spinner1)
        val addSection:EditText = view.findViewById(R.id.addSectionEditText)
        addSectionImage = view.findViewById(R.id.sectionImage)
        val deleteButton:Button = view.findViewById(R.id.deleteSection)
        val addButton:Button = view.findViewById(R.id.addSection)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = AddSectionModelFactory(application)


         val dialog: AlertDialog = SpotsDialog.Builder()
             .setContext(context)
             .setMessage("جارى التحميل")
             .setCancelable(false).build()

        addSectionViewModel = ViewModelProvider(activity!!,viewModelFactory).get(AddSectionViewModel::class.java)
        deleteSectionViewModel = ViewModelProvider(activity!!).get(DeleteSectionViewModel::class.java)

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

        deleteSectionViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            activity!!.viewModelStore.clear()

        })

        deleteSectionViewModel.response.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            TastyToast.makeText(context,it.status, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            addSectionViewModel.getSections()
            sectionsName.remove(spinner.selectedItem)
            adapter.notifyDataSetChanged()
            activity!!.viewModelStore.clear()

        })

        addSectionViewModel.response.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it.message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            dialog.dismiss()
            addSectionViewModel.getSections()
            adapter.notifyDataSetChanged()

        })

        addSectionViewModel.connectionErrorUploadSection.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            activity!!.viewModelStore.clear()

        })

        deleteButton.setOnClickListener {

            dialog.show()
            if (NetworkUtils().isInternetAvailable(activity!!)){
                dialog.show()
                deleteSectionViewModel.deleteSection(sectionName)
            }
            else{
                TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }

        }

        addSectionImage.setOnClickListener {
            selectImage()
        }
        addButton.setOnClickListener {
            if (NetworkUtils().isInternetAvailable(activity!!)){
                dialog.show()
                if (addSection.text.isEmpty() || selectedImage.path.isNullOrEmpty() )
                {
                    TastyToast.makeText(context,"تاكد من البيانات", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()

                }
                else
                {
                    dialog.show()
                    addSectionViewModel.uploadSections(addSection.text.toString(),selectedImage,application)
                }

            }
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