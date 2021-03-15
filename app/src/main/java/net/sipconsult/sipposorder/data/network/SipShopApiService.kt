package net.sipconsult.sipposorder.data.network

import net.sipconsult.sipposorder.data.models.*
import net.sipconsult.sipposorder.data.network.response.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface SipShopApiService {

    @Headers("Content-Type: application/json")
    @GET("api/ProductCategories")
    suspend fun getProductCategoriesAsync(): ProductCategories

    @Headers("Content-Type: application/json")
    @GET("api/Products")
    suspend fun getProductsAsync(): Products

    @Headers("Content-Type: application/json")
    @GET("api/orders")
    suspend fun getOrdersAsync(): Orders

    @Headers("Content-Type: application/json")
    @GET("api/orders/{orderId}")
    suspend fun getOrderAsync(@Path("orderId") orderId: Int): OrderItem

    @Headers("Content-Type: application/json")
    @GET("api/Products/Category/{categoryId}")
    suspend fun getProductsCategoryAsync(@Path("categoryId") categoryId: Int): Products

    @Headers("Content-Type: application/json")
    @GET("api/Locations")
    suspend fun getLocationsAsync(): Locations

    @Headers("Content-Type: application/json")
    @GET("api/PaymentMethods")
    suspend fun getPaymentMethodsAsync(): PaymentMethods

    @POST("api/Account/Authenticate")
    suspend fun authenticateAsync(@Body signInBody: SignInBody): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("api/Orders")
    suspend fun postOrderAsync(@Body body: OrderPostBody): OrderResponse

    @PUT("api/Orders/{id}")
    suspend fun postOrderSAsync(@Path("id") id: Int, @Body body: OrderPutBody): OrderResponse

    @GET("api/SalesAgent")
    suspend fun getSalesAgentsAsync(): SalesAgents

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): SipShopApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", "API_KEY")
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

/*
            val gson = GsonBuilder()
                .setLenient()
                .create()
*/

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://173.248.135.167/sipshop/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SipShopApiService::class.java)
        }
    }
}