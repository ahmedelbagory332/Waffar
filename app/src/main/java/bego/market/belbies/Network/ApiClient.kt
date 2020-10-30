package bego.market.belbies.Network

import bego.market.belbies.Models.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import java.util.concurrent.TimeUnit


class ApiClient {
    private val BASE_URL = "https://belbiesmarket.000webhostapp.com/php/"
    lateinit var  apiInterface: ApiInterface
    private var INSTANCE: ApiClient? = null

     init {
         val okHttpClient = OkHttpClient.Builder()
             .readTimeout(60, TimeUnit.SECONDS)
             .connectTimeout(60, TimeUnit.SECONDS)
             .build()

          val moshi = Moshi.Builder()
             .add(KotlinJsonAdapterFactory())
              .build()
         val retrofit : Retrofit = Retrofit.Builder()
                                        .baseUrl(BASE_URL)
                                        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                                         .client(okHttpClient)
                                         .build()
        apiInterface = retrofit.create(ApiInterface::class.java)
    }

    fun getINSTANCE(): ApiClient? {
        if (null == INSTANCE) {
            INSTANCE = ApiClient()
        }
        return INSTANCE
    }


    fun getProductsSection(section:String) : Call<ProductsSection> = apiInterface.getProductsSection(section)
    fun getProductDetails(id:String) : Call<ProductDetails> = apiInterface.getProductDetails(id)
    fun getOffers() : Call<ProductOffers> = apiInterface.getOffers()
    fun getOffersDetails(id:String) : Call<ProductOfferDetails> = apiInterface.getOffersDetails(id)
    fun addUser( name:String, email:String,password:String) : Call<SignUp> = apiInterface.addUser(name,email,password)
    fun codeVerified(email:String,code:String) : Call<Verified> = apiInterface.codeVerified(email,code)
    fun signIn(email:String,password:String) : Call<SignIn> = apiInterface.signIn(email,password)
    fun addOrder(product_name:String,  product_img:String,  product_total_price:String,  product_user_email:String,user_address:String,product_quantity:String,user_phone:String,user_name:String) : Call<Orders> = apiInterface.addOrder(product_name,  product_img,  product_total_price,  product_user_email,user_address,product_quantity,user_phone,user_name)
    fun getUsersOrders() : Call<AllUsersOrders> = apiInterface.getUsersOrders()
    fun getUserOrder(product_user_email:String) : Call<UserOrders> = apiInterface.getUserOrder(product_user_email)
    fun deleteUserOrders(user_mail:String) : Call<DeleteUserOrders> = apiInterface.deleteUserOrders(user_mail)
    fun deleteOrder(product_number:String) : Call<DeleteUserOrders> = apiInterface.deleteOrder(product_number)
    fun getSections() : Call<Section> = apiInterface.getSections()
    fun deleteSection(sectionName:String) : Call<DeleteUserOrders> = apiInterface.deleteSection(sectionName)
    fun uploadSection(sectionName: RequestBody, picFile: MultipartBody.Part ) : Call<UplaodSection> = apiInterface.uploadSection(sectionName,picFile)
    fun addProduct( product_name: RequestBody?,  product_des: RequestBody?,  product_price: RequestBody?,  product_section: RequestBody?, product_offer_price: RequestBody?,  product_offer_percentage: RequestBody?,  image: MultipartBody.Part?) : Call<UplaodProduct> = apiInterface.addProduct( product_name,  product_des,  product_price,  product_section, product_offer_price,  product_offer_percentage,  image)
    fun editProduct( id : Int ,product_name: String,  product_des: String,  product_price: String,  product_section: String, product_offer_price: String,  product_offer_percentage: String) : Call<UplaodProduct> = apiInterface.editProduct(id, product_name,  product_des,  product_price,  product_section, product_offer_price,  product_offer_percentage)
    fun getAllProduct() : Call<AdminProducts>  = apiInterface.getAllProduct()
    fun addReport(userReport:String,textReport:String) : Call<Report> = apiInterface.addReport(userReport,textReport)
    fun getAllReports() : Call<AllReports> = apiInterface.getAllReports()
    fun deleteReport(id:String) : Call<DeleteUserOrders> = apiInterface.deleteReport(id)
    fun delivery(productUserEmail:String, productNumber:String) : Call<DeleteUserOrders> = apiInterface.delivery(productUserEmail,productNumber)

}
