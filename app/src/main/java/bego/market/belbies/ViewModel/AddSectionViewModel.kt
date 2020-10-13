package bego.market.belbies.ViewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bego.market.belbies.FileUtils
import bego.market.belbies.Models.Section
import bego.market.belbies.Models.UplaodSection
import bego.market.belbies.Network.ApiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class AddSectionViewModel(application: Application) : AndroidViewModel(application) {




    private val _connectionError = MutableLiveData<String>()
    private val _sections = MutableLiveData<Section>()
    private val _response = MutableLiveData<UplaodSection>()
    private val _connectionErrorUploadSection = MutableLiveData<String>()




    val connectionErrorUploadSection: LiveData<String>
        get() = _connectionErrorUploadSection

    val connectionError: LiveData<String>
        get() = _connectionError

    val sections: LiveData<Section>
        get() = _sections

    val response: LiveData<UplaodSection>
        get() = _response




    fun uploadSections(sectionName:String  , fileUri: Uri , application: Application) {


        val sectionNameRequestBody: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), sectionName)

        val fileToSend = prepareFilePart(application, "sectionImg", fileUri)
        ApiClient().getINSTANCE()?.uploadSection(sectionNameRequestBody, fileToSend)?.enqueue(object : Callback<UplaodSection> {
            override fun onResponse(call: Call<UplaodSection>, response: Response<UplaodSection>) {
               _response.value = response.body()
            }

            override fun onFailure(call: Call<UplaodSection>, t: Throwable) {
                    _connectionErrorUploadSection.value  = "حدث خطأ اثناء تحميل المنتجات"
            }
        })


    }


    fun getSections() {


        ApiClient().getINSTANCE()?.getSections()
            ?.enqueue(object : Callback<Section> {
                override fun onFailure(call: Call<Section>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"



                }

                override fun onResponse(call: Call<Section>, response: Response<Section>) {

                        _sections.value = response.body()!!

                }

            })


    }

    fun prepareFilePart(context: Context, partName: String?, fileUri: Uri?): MultipartBody.Part {
        val file: File = FileUtils.getFile(context, fileUri)!!
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse(Objects.requireNonNull(context.contentResolver.getType(fileUri!!))), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


}