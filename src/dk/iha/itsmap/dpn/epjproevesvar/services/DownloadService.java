package dk.iha.itsmap.dpn.epjproevesvar.services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class DownloadService extends AsyncTask<String, Boolean, String>{

	private String result = "";

	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(params[0]+params[1]);
		HttpResponse webServerResponse = null;
		
		request.setHeader(params[2], params[3]);
		
		try {
			webServerResponse = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpEntity httpEntity = webServerResponse.getEntity();

		if(httpEntity != null){
			InputStream inStream;
			try {
				inStream = httpEntity.getContent();
				result = convertStreamToString(inStream);
				inStream.close();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(webServerResponse.getStatusLine().getStatusCode() == 400){
			return "badRequest";
		}else if(webServerResponse.getStatusLine().getStatusCode() == 404){
			return "notFound";
		}
		return result;
	}

	protected void onPostExecute(String result){
	}

	public String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line = null;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				is.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

}