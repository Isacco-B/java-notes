package com.isaccobertoli.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.isaccobertoli.controllers.auth.LoginController;
import com.isaccobertoli.services.ApiService;

import javafx.application.Platform;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final Properties properties = PropertiesUtil.loadProperties("application.properties");
    private static final String BASE_URL = properties.getProperty("BASE_URL").endsWith("/")
            ? properties.getProperty("BASE_URL")
            : properties.getProperty("BASE_URL") + "/";

    private static Retrofit retrofitInstance;
    private static ApiService apiServiceInstance;

    private RetrofitClient() {
    }

    public static synchronized ApiService getService() {
        if (apiServiceInstance == null) {
            if (retrofitInstance == null) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Accept", "application/json");

                                String token = TokenStorage.loadToken();
                                if (token != null) {
                                    requestBuilder.header("Authorization", "Bearer " + token);
                                }

                                Request request = requestBuilder.method(original.method(), original.body()).build();
                                Response response = chain.proceed(request);

                                if (response.code() == 401) {
                                    Platform.runLater(() -> {
                                        TokenStorage.clearToken();
                                        SceneManager.switchScene(new LoginController().getScene(), "Login");
                                    });
                                }

                                return response;
                            }
                        })
                        .build();

                retrofitInstance = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            apiServiceInstance = retrofitInstance.create(ApiService.class);
        }
        return apiServiceInstance;
    }
}
