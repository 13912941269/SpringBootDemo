package com.chemguan.util;


import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class OKHttpSingle {

	private static class LazyHolder {
		private static final OkHttpClient INSTANCE = new OkHttpClient.Builder()
				.connectTimeout(200000, TimeUnit.MILLISECONDS)
				.readTimeout(200000, TimeUnit.MILLISECONDS)
				.build();
	}
	private OKHttpSingle(){}
	public static final OkHttpClient getInstance() {
		return LazyHolder.INSTANCE;
	}

}
