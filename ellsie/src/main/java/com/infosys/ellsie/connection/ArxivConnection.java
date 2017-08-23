package com.infosys.ellsie.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.util.Constants;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class ArxivConnection {
	
	private static final Logger LOG = LoggerFactory.getLogger(ArxivConnection.class);

	private OkHttpClient client;

	private HttpUrl listUrl = new HttpUrl.Builder().scheme("http").host("export.arxiv.org").addPathSegment("oai2")
			.addQueryParameter("metadataPrefix", "arXiv").addQueryParameter("verb", "ListIdentifiers").build();

	private HttpUrl getUrl = new HttpUrl.Builder().scheme("http").host("export.arxiv.org").addPathSegment("oai2")
			.addQueryParameter("metadataPrefix", "arXiv").addQueryParameter("verb", "GetRecord").build();

	public ArxivConnection() {

		int proxyPort = 80;
		String proxyHost = "10.68.248.102";
		final String username = Constants.USERNAME;
		final String password = Constants.PASSWORD;

		Authenticator proxyAuthenticator = new Authenticator() {
			public Request authenticate(Route route, Response response) throws IOException {
				String credential = Credentials.basic(username, password);
				return response.request().newBuilder().header("Proxy-Authorization", credential).build();
			}
		};

		// retry mechanism
		Interceptor retryInterceptor = new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request request = chain.request();
				Response response = null;
				boolean responseOK = false;
				int tryCount = 0;

				while (!responseOK && tryCount < 3) {
					try {
						response = chain.proceed(request);
						responseOK = response.isSuccessful();
					} catch (Exception e) {
						LOG.info("intercept: Request is not successful - {}" + tryCount);
					} finally {
						tryCount++;
					}
				}

				// otherwise just pass the original response on
				return response;
			}
		};

		this.client = new OkHttpClient.Builder()
				.connectTimeout(60, TimeUnit.SECONDS)
				.writeTimeout(60, TimeUnit.SECONDS)
				.readTimeout(60, TimeUnit.SECONDS)
				.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
				.proxyAuthenticator(proxyAuthenticator)
				.addInterceptor(retryInterceptor)
				.build();
	}

	public String listIdentifiers(Date fromDate, Date toDate) throws IOException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		HttpUrl url = listUrl.newBuilder().addQueryParameter("from", df.format(fromDate))
				.addQueryParameter("until", df.format(toDate)).build();

		LOG.info("Querying repository: {}" , url);

		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		return response.body().string();

	}

	public String getListIdentifiers() throws IOException {

		LOG.info("Querying repository: {}" , listUrl);

		Request request = new Request.Builder().url(listUrl).build();

		Response response = client.newCall(request).execute();
		return response.body().string();

	}

	public String getRecord(String identifier) throws IOException {
		HttpUrl url = this.getUrl.newBuilder().addQueryParameter("identifier", identifier).build();

		LOG.info("Querying repository: {}" , url);

		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

}
