package com.browser2app.pse.psenativedemo;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PSENativeDemo extends Application {

	AutomatonServerInterface automatonServer;

	public PSENativeDemo() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
		httpClientBuilder.addInterceptor(logging);  // <-- this is the important line!
		automatonServer = new Retrofit.Builder()
				.baseUrl("https://b2a.pse.com.co/api/automata/")
				.client(httpClientBuilder.build())
				.addConverterFactory(GsonConverterFactory.create())
				.build().create(AutomatonServerInterface.class);
	}

	public AutomatonServerInterface getAutomatonServer() {
		return automatonServer;
	}
}
