package bego.market.waffar.di


import android.app.Application
import androidx.room.Room
import bego.market.waffar.data.localDataBase.CartDatabase
import bego.market.waffar.data.network.ApiInterface
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient,gson: Gson): Retrofit =
            Retrofit.Builder()
            .baseUrl(ApiInterface.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()


    @Provides
    @Singleton
    fun provideRestaurantApi(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application) : CartDatabase =
        Room.databaseBuilder(app, CartDatabase::class.java, "cart_database")
            .build()


}