/**
 * 
 */
package com.infosys.ellsie.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Ankur_Jain22
 *
 */
public class RestClient {
	
	private static final MediaType MediaTypeJSON = MediaType.parse("application/json; charset=utf-8");
    private static final String URI_SOLR = "http://localhost:8983/solr/schema";
    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		OkHttpClient httpclient = new OkHttpClient();
		
		// json params
		
		Map<String,String> payload = new HashMap<>();
		payload.put("key1","value1");
		payload.put("key2","value2");

		//String jsonRequest = new ObjectMapper().writeValueAsString(payload);
		String jsonRequest = null;
		LOG.info(jsonRequest);
		
		
		// build a request
        Request request = new Request.Builder().url(URI_SOLR)
                .post(RequestBody.create(MediaTypeJSON, jsonRequest)).build();
        try (Response response = httpclient.newCall(request).execute()) {
 
            if (response.isSuccessful()) {
            	LOG.info("Success: {}", response.toString());
            }
        }

	}

}
