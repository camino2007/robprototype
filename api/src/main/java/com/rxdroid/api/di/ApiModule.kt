import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rxdroid.api.github.GitHubApi
import com.rxdroid.api.github.details.DetailsApiProvider
import com.rxdroid.api.github.details.DetailsProviderImpl
import com.rxdroid.api.github.search.SearchApiProvider
import com.rxdroid.api.github.search.SearchProviderImpl
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {

    single { provideGson() }

    single { provideCache(androidContext()) }

    single { provideOkHttpClient(get()) }

    single { provideRetrofit(get(), get()) }

    single { provideGitHubApi(get()) }

    single { SearchProviderImpl(get()) as SearchApiProvider }

    single { DetailsProviderImpl(get()) as DetailsApiProvider }

}

private const val TIME_OUT = 30L

private fun provideGson(): Gson {
    return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
}

private fun provideOkHttpClient(cache: Cache): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(interceptor)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .cache(cache)
            .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
}

private fun provideGitHubApi(retrofit: Retrofit): GitHubApi {
    return retrofit.create(GitHubApi::class.java)
}

private fun provideCache(androidContext: Context): Cache {
    val cacheSize = 10 * 1024 * 1024 // 10 MiB
    return Cache(androidContext.cacheDir, cacheSize.toLong())
}

