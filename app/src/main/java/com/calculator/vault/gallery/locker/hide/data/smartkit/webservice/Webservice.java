package com.calculator.vault.gallery.locker.hide.data.smartkit.webservice;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class Webservice {

	public static String Send_data(HashMap<String, String> valueMap, String url) {
		HttpRequest httpRequest = new HttpRequest();
		// HashMap<String, String> valueMap = new HashMap<String, String>();
		String response = "";
		try {

			// valueMap.clear();
			// valueMap.put("id_ad", id_ad);
			// valueMap.put("email", email);
			// valueMap.put("phone", phone);
			// valueMap.put("msg_text", msg_text);

			response = httpRequest.doPost(url,valueMap);

			System.out.println("Response  ----" + response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	public static String convertStreamToString(InputStream is)
			throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}

	}

	public static String Send_message(HashMap<String, String> valueMap,
                                      String url, String filekey, String filepath) {

		HttpRequest httpRequest = new HttpRequest();
		// HashMap<String, String> valueMap = new HashMap<String, String>();
		String response = "";

		try {

			// valueMap.clear();
			// valueMap.put("id_ad", id_ad);
			// valueMap.put("email", email);
			// valueMap.put("phone", phone);
			// valueMap.put("msg_text", msg_text);

			response = httpRequest.doPostWithFile(url, filekey, filepath,valueMap);

			System.out.println("Response  ----" + response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}
	// convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }


	public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        
        URI url_main;
		String login1 = null;

        try {
        	login1 = url;
        	url_main = new URI(login1.replace(" ", "%20"));
			Log.e("my webservice",""+url_main);
            // create HttpClient


            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }

	public static String GET(URL url){
		InputStream inputStream = null;
		String result = "";

		URI url_main;
		URL login1 = null;

		try {
			login1 = url;
//        	url_main = new URI(login1.replace(" ", "%20"));
			Log.e("my webservice",""+login1);
			// create HttpClient

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(false);
			urlConnection.setRequestMethod("GET");
			try {
				urlConnection.setChunkedStreamingMode(0);
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				result = convertInputStreamToString(in);

			} finally {
				urlConnection.disconnect();
				Log.e("disconnect","disconnect");
			}

            /*HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";*/

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
}
