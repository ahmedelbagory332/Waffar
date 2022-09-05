package bego.market.waffar.ui.fragments.admin.section

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import bego.market.waffar.utils.FileUtils
import bego.market.waffar.models.Section
import bego.market.waffar.models.UploadModel
import bego.market.waffar.data.network.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddSectionViewModel @Inject constructor(private val api: ApiInterface,private val ctx :Application):ViewModel() {




    private val _connectionError = MutableStateFlow("")
    private val _sections = MutableStateFlow<MutableList<Section>>(mutableListOf())
    private val _response = MutableStateFlow(UploadModel(false,""))


    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val sections: LiveData<MutableList<Section>>
        get() = _sections.asLiveData()

    val response: LiveData<UploadModel>
        get() = _response.asLiveData()



    fun restVariables() {
        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("")
            _response.emit(UploadModel(false,""))
        }
    }


    fun uploadSections(sectionName:String  , fileUri: Uri) {

        val sectionNameRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), sectionName)
        val fileToSend = prepareFilePart(ctx, "sectionImg", fileUri)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.uploadSection(sectionNameRequestBody, fileToSend)
                if (response.isSuccessful) {
                    _response.emit(response.body()!!)
                } else {
                    _connectionError.emit("حدث خطأ")
                }
            }catch (e:Exception){
                _connectionError.emit("حدث خطأ")
            }

        }

    }


    fun getSections() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getSections()
                if (response.isSuccessful) {
                    _sections.emit(response.body()!!.sections)
                } else {
                    _connectionError.emit("حدث خطأ")
                }
            }catch (e:Exception){
                _connectionError.emit("حدث خطأ")

            }

        }



    }

    private fun prepareFilePart(context: Context, partName: String, fileUri: Uri): MultipartBody.Part {
        val file: File = FileUtils.getFile(context, fileUri)
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse(Objects.requireNonNull(context.contentResolver.getType(fileUri))), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


}