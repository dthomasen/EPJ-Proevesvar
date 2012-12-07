package dk.iha.itsmap.dpn.epjproevesvar.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.google.gson.Gson;

import dk.iha.itsmap.dpn.epjproevesvar.business.AddUpdateFavoritePatient;
import dk.iha.itsmap.dpn.epjproevesvar.business.Color;

public class AddFavoriteToServer {
	
	private static final String TAG="AddFavoriteToServer";  	
	
	public void addFavoriteToServer(String cpr) {
		Gson gson = new Gson();
		String json = gson.toJson(new AddUpdateFavoritePatient(cpr,Color.WHITE)); 
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost("http://130.225.184.205:8082/pfpas-mobile/rest/favorites/"+cpr);
		HttpResponse webServerResponse = null;
		
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("CPR", cpr));
		nameValuePairs.add(new BasicNameValuePair("JSON", json));
		Log.d(TAG, "Json: "+json);
		
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
		}
		
		try {
			webServerResponse = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
}
