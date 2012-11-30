package dk.iha.itsmap.dpn.epjproevesvar;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;


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
    private HashMap<String, Favorite> favoritesMap = new HashMap<String, Favorite>();
    private Favorite[] favorites = null;

    public class GetFavoritesBinder extends Binder {
        FavoritesDownloadServices getService() {
            return FavoritesDownloadServices.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    public HashMap<String, Favorite> getFavoritesMap(){
    	return favoritesMap;
    }
    
    public Favorite[] getFavorites(){
    	return favorites;
    }
    
    /** method for clients */
    public void updateFavorites() {
    	try {
    		result = new AsyncFavoritePatientsDownload().execute("http://130.225.184.205:8082/","pfpas-mobile/rest/favorites/","Authorization","Basic c3V2OnNpa2tlcmhlZA==").get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	
  		favorites = new Gson().fromJson(result, Favorite[].class);
  		favoritesMap.clear();
  		for(Favorite s : favorites){
  			favoritesMap.put(s.getName(), s);
  		}
  		Intent i = new Intent("FavoritesUpdated");
  		
  		sendBroadcast(i);
  	}

}
