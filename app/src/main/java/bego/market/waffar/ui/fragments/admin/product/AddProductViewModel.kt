package bego.market.waffar.ui.fragments.admin.product

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import bego.market.waffar.utils.FileUtils
import bego.market.waffar.data.network.ApiInterface
import bego.market.waffar.models.UploadModel
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
class AddProductViewModel @Inject constructor(private val api: ApiInterface, private val ctx:Application) : ViewModel() {



    private val _connectionError = MutableStateFlow("")
    private val _response = MutableStateFlow(UploadModel(false,""))



    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()


    val response: LiveData<UploadModel>
        get() = _response.asLiveData()


    fun restAddProductVariables() {
        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("")
            _response.emit(UploadModel(false,""))
        }
    }

    fun uploadProduct( product_name:String,  product_des:String,  product_price:String,  product_section:String, product_offer_price:String,  product_offer_percentage:String, fileUri: Uri) {


        val productNameRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_name)
        val productDescriptionRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_des)
        val productPriceRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_price)
        val productSectionRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_section)
        val productOfferPriceRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_offer_price)
        val productOfferPercentageRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_offer_percentage)

        val fileToSend = prepareFilePart("product_img", fileUri)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.addProduct(productNameRequestBody,productDescriptionRequestBody,productPriceRequestBody,productSectionRequestBody,productOfferPriceRequestBody,productOfferPercentageRequestBody, fileToSend)
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



    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file: File = FileUtils.getFile(ctx, fileUri)
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse(Objects.requireNonNull(ctx.contentResolver.getType(fileUri))), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


}