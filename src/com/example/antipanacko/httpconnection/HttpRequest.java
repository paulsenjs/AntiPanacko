package com.example.antipanacko.httpconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpRequest {
 
	public static String get(String sUrl) throws MalformedURLException, IOException {
		
//		String result = null;
		
		HttpClient client = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(sUrl);
		httpGet.addHeader("Cache-Control", "no-cache");
		
		HttpResponse response = client.execute(httpGet);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
	    }
		
		return buffer.toString();
	}
	
	public static String post(String sUrl, String data) throws MalformedURLException, IOException {
		
		String result = null;
		
		URL url = new URL(sUrl);
		HttpURLConnection connUrl = (HttpURLConnection) url.openConnection();
		connUrl.setDoOutput(true);
		
		OutputStreamWriter writer = new OutputStreamWriter(connUrl.getOutputStream());
		writer.write(data);
		writer.flush();
		
		InputStreamReader reader = new InputStreamReader(connUrl.getInputStream());
		BufferedReader in = new BufferedReader(reader);
		
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) { 
			builder.append(line);
		}
		result = builder.toString();
		
		reader.close();
		writer.close();
		
		return result;
	}
	
	public static String post(String sUrl, Hashtable<String, String> params) throws UnsupportedEncodingException, MalformedURLException, IOException {
		
		String key = null;
		Enumeration<String> keys = params.keys();
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(sUrl);
		post.addHeader("Cache-Control", "no-cache");
		
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		while (keys.hasMoreElements()) {
			key = (String) keys.nextElement();
			param.add(new BasicNameValuePair(key, params.get(key)));
		}
		post.setEntity(new UrlEncodedFormEntity(param));
		HttpResponse response = client.execute(post);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
	    }
		
		return buffer.toString();
	}
	
	public static String delete (String sUrl) throws MalformedURLException, IOException {		
		HttpClient client = new DefaultHttpClient();
		HttpDelete delete = new HttpDelete(sUrl);
		delete.addHeader("Cache-Control", "no-cache");
		HttpResponse response = client.execute(delete);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
	    }
		
		return buffer.toString();
	}
}
