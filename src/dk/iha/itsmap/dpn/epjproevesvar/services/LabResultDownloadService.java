package dk.iha.itsmap.dpn.epjproevesvar.services;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;

import dk.iha.itsmap.dpn.epjproevesvar.business.LabResult;
import dk.iha.itsmap.dpn.epjproevesvar.screens.Favorite;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LabResultDownloadService extends Service{
	
	private static final String TAG="LabResultDownloadService";
	 // Binder given to clients
    private final IBinder mBinder = new GetLabResultBinder();
    private String result = "";
    private LabResult labResults = null;

    public class GetLabResultBinder extends Binder {
        public LabResultDownloadService getService() {
            return LabResultDownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    public LabResult getLabResults(){
    	return labResults;
    }
    
    /** method for clients */
    public void updateLabResults(String authorization, String cprNumber) {
    	try {
    		Log.d(TAG, "Updating lab results");
    		Log.d(TAG, "Authorization: "+authorization);
    		result = new AsyncFavoritePatientsDownload().execute("http://130.225.184.205:8082/","pfpas-mobile/rest/results/"+cprNumber+"/","Authorization","Basic "+authorization).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	
    	Log.d(TAG,result);
  		labResults = new Gson().fromJson(result, LabResult.class);
  		
  		Intent i = new Intent("LabResultsUpdated");
  		
  		sendBroadcast(i);
  	}

}
