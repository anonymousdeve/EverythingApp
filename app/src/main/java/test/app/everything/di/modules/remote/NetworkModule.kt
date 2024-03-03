package test.app.everything.di.modules.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import test.app.everything.BuildConfig
import test.app.everything.features.list.data.remote.ApiService
import test.app.everything.network.NetworkConnectionInterceptor
import test.app.everything.network.NetworkHeaderInterceptor
import test.app.everything.network.QueryParameterInterceptor


/**
 * Dagger module providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides Gson instance for JSON serialization/deserialization.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    /**
     * Provides GsonConverterFactory for Retrofit.
     */
    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    /**
     * Provides HttpLoggingInterceptor for logging HTTP request/response details.
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context = context).build()
    }


    /**
     * Provides an instance of the QueryParameterInterceptor for adding query parameters to HTTP requests.
     * a singleton instance of the QueryParameterInterceptor. The interceptor is typically used to add
     * query parameters to HTTP requests before they are sent.
     * @return An instance of the QueryParameterInterceptor.
     * @see QueryParameterInterceptor
     */
    @Provides
    @Singleton
    fun provideQueryParameterInterceptor(): QueryParameterInterceptor = QueryParameterInterceptor()


    /**
     * Provides OkHttpClient for making HTTP requests.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        connectivityInterceptor: NetworkConnectionInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        networkHeaderInterceptor: NetworkHeaderInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        queryParameterInterceptor: QueryParameterInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkHeaderInterceptor)
            .addInterceptor(queryParameterInterceptor)
            .apply {
                if (BuildConfig.DEBUG)
                    this.addInterceptor(chuckerInterceptor)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

            .build()
    }

    /**
     * Provides Retrofit instance for API calls.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    /**
     * Provides API service interface implementation.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}