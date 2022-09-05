package bego.market.waffar.data.network


 import bego.market.waffar.models.*
 import okhttp3.MultipartBody
 import okhttp3.RequestBody
 import retrofit2.Response
 import retrofit2.http.*


interface ApiInterface {

    companion object {
        const val BASE_URL = "" // your server url
    }

    @Headers("Accept: application/json")
    @GET("getProductsSection.php")
    suspend fun getProductsSection(@Query("product_section") section:String) : Response<AllProductModel>

    @Headers("Accept: application/json")
    @GET("getProductDetails.php")
    suspend fun getProductDetails(@Query("id") id:String) : Response<ProductDetails>

    @Headers("Accept: application/json")
    @GET("getProductsOffers.php")
     suspend fun getOffers() : Response<OfferModel>


    @Headers("Accept: application/json")
    @GET("getProductOffersDetails.php")
     suspend fun getOffersDetails(@Query("id") id:String) : Response<ProductOfferDetails>

    @FormUrlEncoded
    @POST("signup.php")
    suspend fun addUser(@Field("name") name:String, @Field("email") email:String,@Field("password") password:String) : Response<SignUp>

    @FormUrlEncoded
    @POST("verified.php")
    suspend fun codeVerified(@Field("email") email:String,@Field("code") code:String) : Response<Verified>

    @FormUrlEncoded
    @POST("login.php")
    suspend fun signIn(@Field("email") email:String,@Field("password") password:String) : Response<SignIn>

    @FormUrlEncoded
    @POST("orders.php")
    suspend fun addOrder(@Field("product_name") product_name:String,@Field("product_img") product_img:String,@Field("product_total_price") product_total_price:String,@Field("product_user_email") product_user_email:String,@Field("user_address") user_address:String,@Field("product_quantity") product_quantity:String,@Field("user_phone") user_phone:String,@Field("user_name") user_name:String) : Response<Orders>

    @Headers("Accept: application/json")
    @GET("getUsersOrders.php")
     suspend fun getUsersOrder() : Response<UsersOrdersModel>

    @Headers("Accept: application/json")
    @GET("getUserOrder.php")
     suspend fun getUserOrders(@Query("product_user_email") product_user_email:String) : Response<UserOrdersModel>

    @Headers("Accept: application/json")
    @GET("deleteUsersOrders.php")
     suspend fun deleteUserOrders(@Query("user_mail") user_mail:String) : Response<DeleteModel>

    @Headers("Accept: application/json")
    @GET("deleteOrder.php")
     suspend fun deleteOrder(@Query("product_number") product_number:String) : Response<DeleteModel>

    @Multipart
    @POST("addProduct.php")
     suspend fun addProduct(@Part("product_name") product_name: RequestBody?,@Part("product_des") product_des: RequestBody?,@Part("product_price") product_price: RequestBody?,@Part("product_section") product_section: RequestBody?,@Part("product_offer_price") product_offer_price: RequestBody?,@Part("product_offer_percentage") product_offer_percentage: RequestBody?,@Part image: MultipartBody.Part?) : Response<UploadModel>

    @FormUrlEncoded
    @POST("editProduct.php")
     suspend fun editProduct(@Field("id") id: Int,@Field("product_name") product_name: String,@Field("product_des") product_des: String,@Field("product_price") product_price: String,@Field("product_section") product_section: String,@Field("product_offer_price") product_offer_price: String,@Field("product_offer_percentage") product_offer_percentage: String) : Response<UploadModel>


    @Headers("Accept: application/json")
    @GET("getSections.php")
    suspend fun getSections() : Response<SectionsModel>


    @Headers("Accept: application/json")
    @GET("deleteSection.php")
     suspend fun deleteSection(@Query("sectionName") sectionName:String) : Response<DeleteModel>

    @Multipart
    @POST("addSection.php")
     suspend fun uploadSection(@Part("sectionName") sectionName: RequestBody?, @Part image: MultipartBody.Part?): Response<UploadModel>

    @Headers("Accept: application/json")
    @GET("getAllProducts.php")
     suspend fun getAllProduct() : Response<AllProductModel>

    @FormUrlEncoded
    @POST("addReport.php")
     suspend fun addReport(@Field("userReport") userReport:String,@Field("textReport") textReport:String) : Response<Report>

    @Headers("Accept: application/json")
    @GET("getAllReports.php")
    suspend fun getAllReports() : Response<ReportsModel>

    @Headers("Accept: application/json")
    @GET("deleteReport.php")
    suspend fun deleteReport(@Query("id") id:String) : Response<DeleteModel>

    @FormUrlEncoded
    @POST("delivery.php")
     suspend fun delivery(@Field("productUserEmail") productUserEmail:String,@Field("productNumber") productNumber:String): Response<DeliveryModel>
}