package com.calculator.vault.gallery.locker.hide.data.commonCode.utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xml.sax.InputSource;

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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Webservice {
	private static String TAG = Webservice.class.getSimpleName();

	public static String webServiceCall(String strUrl, String operation) throws IOException {
		URL url = null;
		String response = null;

		try {
			strUrl = strUrl.replace(" ", "%20");
			url = new URL(strUrl);

			System.out.println(TAG + "start webservices");
			Log.e(TAG , "URL : " + url);
			InputSource doc = new InputSource(url.openStream());
			response = convertStreamToString(doc.getByteStream());
			Log.e(TAG, "RESPONSE : " + response);
			System.out.println(TAG + "end webservices");

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

	public static String Send_message(HashMap<String, String> valueMap, String url, String filekey, String filepath) {
		HttpRequest httpRequest = new HttpRequest();
		String response = "";

		try {
			response = httpRequest.doPostWithFile(url, filekey, filepath,valueMap);
			System.out.println(TAG +" => Response  ----" + response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static String Send_data(HashMap<String, String> valueMap, String url) {

		HttpRequest httpRequest = new HttpRequest();

		String response = "";
		try {
			response = httpRequest.doPost(url, valueMap);
			System.out.println(TAG +" => Response  ----" + response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static String get_response(String url_main) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = "";
		URI url = null;
		String login1 = null;

		try {
			login1 = url_main;
			url = new URI(login1.replace(" ", "%20"));

			Log.e(TAG, "Url : " + url);

			HttpPost httppost = new HttpPost(url);
			HttpResponse httpResponse = null;

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httppost);

				response = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out.println(TAG +" => Response  ----" + response);

			} catch (ClientProtocolException e) {
				Log.e(TAG , "Error : " + e);
			} catch (IOException e) {
				Log.e(TAG ,"Error : " + e);
			}
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		return response;
	}
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
			Log.e("my webservice", "Login : " + url_main);

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

	public static String search_mapview(String city_name) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		String login1 = "";
		try {



			url = new URI(login1.replace(" ", "%20"));

			Log.e("my webservice", "search_map : " + url);
			Log.e(TAG , "URL : " + url);
			HttpPost httppost = new HttpPost(url);
			HttpResponse httpResponse = null;

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httppost);
				response = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database

				System.out.println(TAG +" => Response  ----" + response);

			} catch (ClientProtocolException e) {
				Log.e(TAG , "Error : " + e);
			} catch (IOException e) {
				Log.e(TAG ,"Error : " + e);
			}
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		return response;
	}
	public static String postData(String url, JSONObject obj) {
		// Create a new HttpClient and Post Header
		String res = "";
		HttpParams myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);
		HttpClient httpclient = new DefaultHttpClient(myParams );
		try {

			Log.i("tag",url.toString()+obj.toString());
			HttpPost httppost = new HttpPost(url.toString());
			httppost.setHeader("Content-type", "multipart/form-data");

			StringEntity se = new StringEntity(obj.toString());
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8"));
			httppost.setEntity(se);
			HttpResponse response = httpclient.execute(httppost);
			res = EntityUtils.toString(response.getEntity());
			Log.i("tag", res);

		} catch (ClientProtocolException e) {

		} catch (IOException e) {
		}
		return res;
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
