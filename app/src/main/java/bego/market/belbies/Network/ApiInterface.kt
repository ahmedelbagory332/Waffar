package bego.market.belbies.Network


 import bego.market.belbies.Models.*
 import okhttp3.MultipartBody
 import okhttp3.RequestBody
 import retrofit2.Call
 import retrofit2.http.*


interface ApiInterface {


    @Headers("Accept: application/json")
    @GET("getProductsSection.php")
    fun getProductsSection(@Query("product_section") section:String) : Call<ProductsSection>

    @Headers("Accept: application/json")
    @GET("getProductDetails.php")
    fun getProductDetails(@Query("id") id:String) : Call<ProductDetails>

    @Headers("Accept: application/json")
    @GET("getProductsOffers.php")
    fun getOffers() : Call<ProductOffers>


    @Headers("Accept: application/json")
    @GET("getProductOffersDetails.php")
    fun getOffersDetails(@Query("id") id:String) : Call<ProductOfferDetails>

    @FormUrlEncoded
    @POST("signup.php")
    fun addUser(@Field("name") name:String, @Field("email") email:String,@Field("password") password:String) : Call<SignUp>

    @FormUrlEncoded
    @POST("verified.php")
    fun codeVerified(@Field("email") email:String,@Field("code") code:String) : Call<Verified>

    @FormUrlEncoded
    @POST("login.php")
    fun signIn(@Field("email") email:String,@Field("password") password:String) : Call<SignIn>

    @FormUrlEncoded
    @POST("orders.php")
    fun addOrder(@Field("product_name") product_name:String,@Field("product_img") product_img:String,@Field("product_total_price") product_total_price:String,@Field("product_user_email") product_user_email:String,@Field("user_address") user_address:String,@Field("product_quantity") product_quantity:String) : Call<Orders>

    @Headers("Accept: application/json")
    @GET("getUsersOrders.php")
    fun getUsersOrders() : Call<AllUsersOrders>

    @Headers("Accept: application/json")
    @GET("getUserOrder.php")
    fun getUserOrder(@Query("product_user_email") product_user_email:String) : Call<UserOrders>

    @Headers("Accept: application/json")
    @GET("deleteUsersOrders.php")
    fun deleteUserOrders(@Query("user_mail") user_mail:String) : Call<DeleteUserOrders>

    @Headers("Accept: application/json")
    @GET("deleteOrder.php")
    fun deleteOrder(@Query("product_number") product_number:String) : Call<DeleteUserOrders>

    @Multipart
    @POST("addProduct.php")
    fun addProduct(@Part("product_name") product_name: RequestBody?,@Part("product_des") product_des: RequestBody?,@Part("product_price") product_price: RequestBody?,@Part("product_section") product_section: RequestBody?,@Part("product_offer_price") product_offer_price: RequestBody?,@Part("product_offer_percentage") product_offer_percentage: RequestBody?,@Part image: MultipartBody.Part?) : Call<UplaodProduct>

    @FormUrlEncoded
    @POST("editProduct.php")
    fun editProduct(@Field("id") id: Int,@Field("product_name") product_name: String,@Field("product_des") product_des: String,@Field("product_price") product_price: String,@Field("product_section") product_section: String,@Field("product_offer_price") product_offer_price: String,@Field("product_offer_percentage") product_offer_percentage: String) : Call<UplaodProduct>


    @Headers("Accept: application/json")
    @GET("getSections.php")
    fun getSections() : Call<Section>


    @Headers("Accept: application/json")
    @GET("deleteSection.php")
    fun deleteSection(@Query("sectionName") sectionName:String) : Call<DeleteUserOrders>

    @Multipart
    @POST("addSection.php")
    fun uploadSection(@Part("sectionName") sectionName: RequestBody?, @Part image: MultipartBody.Part?): Call<UplaodSection>

    @Headers("Accept: application/json")
    @GET("getAllProducts.php")
    fun getAllProduct() : Call<AdminProducts>

    @FormUrlEncoded
    @POST("addReport.php")
    fun addReport(@Field("userReport") userReport:String,@Field("textReport") textReport:String) : Call<Report>

    @Headers("Accept: application/json")
    @GET("getAllReports.php")
    fun getAllReports() : Call<AllReports>

    @Headers("Accept: application/json")
    @GET("deleteReport.php")
    fun deleteReport(@Query("id") id:String) : Call<DeleteUserOrders>

    @FormUrlEncoded
    @POST("delivery.php")
    fun delivery(@Field("productUserEmail") productUserEmail:String,@Field("productNumber") productNumber:String): Call<DeleteUserOrders>
}