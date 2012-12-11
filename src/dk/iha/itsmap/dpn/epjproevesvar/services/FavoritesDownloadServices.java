package dk.iha.itsmap.dpn.epjproevesvar.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;

import dk.iha.itsmap.dpn.epjproevesvar.business.Favorite;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class FavoritesDownloadServices extends Service{
	
	private static final String TAG="FavoritesDownloadService";
	 // Binder given to clients
    private final IBinder mBinder = new GetFavoritesBinder();
    private String result = "";
    private ArrayList<Favorite> favorites = new ArrayList<Favorite>();
	private Favorite[] favoritesList;

    public class GetFavoritesBinder extends Binder {
        public FavoritesDownloadServices getService() {
            return FavoritesDownloadServices.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    public ArrayList<Favorite> getFavorites(){
    	return favorites;
    }
    
    /** method for clients */
    public void updateFavorites(String authorization) {
    	try {
    		result = new DownloadService().execute("http://130.225.184.205:8082/","pfpas-mobile/rest/favorites/","Authorization","Basic "+authorization).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	
  		favoritesList = new Gson().fromJson(result, Favorite[].class);
  		for(Favorite s : favoritesList){
  			favorites.add(s);
  		}
  		Intent i = new Intent("FavoritesUpdated");
  		
  		sendBroadcast(i);
  	}

}
