package fup.prototype.domain.di;

import android.content.Context;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import fup.prototype.domain.github.GitHubApi;
import fup.prototype.domain.github.provider.GitHubProvider;
import fup.prototype.domain.github.provider.GitHubRepoProvider;
import fup.prototype.domain.github.provider.GitHubUserProvider;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DomainModule {

    private static final int TIME_OUT = 30;

    @Provides
    @Singleton
    Cache provideOkHttpCache(final Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(context.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
       final  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(final OkHttpClient okHttpClient, final Gson gson) {
        return new Retrofit.Builder().baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    GitHubApi provideGithubApi(final Retrofit retrofit) {
        return retrofit.create(GitHubApi.class);
    }

    @Provides
    @Singleton
    GitHubProvider provideGitHubProvider(final GitHubApi gitHubApi) {
        return new GitHubProvider(gitHubApi);
    }

    @Provides
    @Singleton
    GitHubUserProvider provideGitHubUserProvider(final GitHubApi gitHubApi) {
        return new GitHubUserProvider(gitHubApi);
    }

    @Provides
    @Singleton
    GitHubRepoProvider provideGitHubRepoProvider(final GitHubApi gitHubApi) {
        return new GitHubRepoProvider(gitHubApi);
    }
}


