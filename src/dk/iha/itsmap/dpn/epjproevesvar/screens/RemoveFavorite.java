package dk.iha.itsmap.dpn.epjproevesvar.screens;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import dk.iha.itsmap.dpn.epjproevesvar.R;
import dk.iha.itsmap.dpn.epjproevesvar.R.layout;
import dk.iha.itsmap.dpn.epjproevesvar.R.menu;
import dk.iha.itsmap.dpn.epjproevesvar.business.AddUpdateFavoritePatient;
import dk.iha.itsmap.dpn.epjproevesvar.business.Color;
import dk.iha.itsmap.dpn.epjproevesvar.business.Favorite;
import dk.iha.itsmap.dpn.epjproevesvar.services.FavoritesDownloadServices;
import dk.iha.itsmap.dpn.epjproevesvar.services.PatientBaseAdapter;
import dk.iha.itsmap.dpn.epjproevesvar.services.FavoritesDownloadServices.GetFavoritesBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class RemoveFavorite extends ListActivity implements OnItemClickListener {

	private static final String TAG="RemoveFavorites";
	private String authorization;
	private ListView favoritesList;
	private ArrayList<Favorite> favorites = new ArrayList<Favorite>();
	private PatientBaseAdapter adapter;
	private BroadcastReceiver updateReciever;
	private FavoritesDownloadServices getFavoritesService;
	protected HashMap<String, Favorite> favoriteMap;
	private boolean mBound = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_favorite);
		authorization = getIntent().getExtras().getString("Authorization");
		favoritesList = getListView();
		adapter = new PatientBaseAdapter(this, favorites);
		favoritesList.setAdapter(adapter);
		favoritesList.setOnItemClickListener(this);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		updateReciever = new BroadcastReceiver() {
			private ArrayList<Favorite> favorites;

			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d(TAG,"FavoritesUpdated broadcast recieved");
				favorites = getFavoritesService.getFavorites();
				adapter.sendList(favorites);
				adapter.notifyDataSetChanged();
			}
		};

		this.registerReceiver(updateReciever, new IntentFilter("FavoritesUpdated"));
	}
	
	protected void onStart(){
		super.onStart();
		Log.d(TAG,"OnStart called");
		Intent intent = new Intent(this, FavoritesDownloadServices.class);
        bindService(intent, connectionToService, Context.BIND_AUTO_CREATE);
        this.registerReceiver(updateReciever, new IntentFilter("FavoritesUpdated"));
	}
	
	private ServiceConnection connectionToService = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
        	GetFavoritesBinder binder = (GetFavoritesBinder) service;
            getFavoritesService = binder.getService();
            mBound = true;
            getFavoritesService.updateFavorites(authorization);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void onPause() {
	     super.onPause();
	     Log.d(TAG,"OnPause called");
	     overridePendingTransition(0, 0);
	}
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"OnStop called");
        if (mBound) {
            unbindService(connectionToService);
            mBound = false;
        }
        unregisterReceiver(updateReciever);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_remove_favorite, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Log.d(TAG,"StationsListItem clicked");
		Favorite choosenFavorite = (Favorite) favoritesList.getItemAtPosition(position);
		buildDialog(choosenFavorite.getCpr(), choosenFavorite.getName());
		
	}
	
	public void buildDialog(final String cpr, String name){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setTitle("Are you sure?");

		String alert1 = "Removing following patient from favorites: ";
		String alert2 = "Name: " + name;
		String alert3 = "CPR: " + cpr;

		alertDialogBuilder
		.setMessage(alert1 +"\n\n"+ alert2 +"\n"+ alert3)
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				new Thread(){
					public void run(){
						removeFavoriteFromServer(cpr);
					}
				}.start();
				RemoveFavorite.this.finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}
	
	public void removeFavoriteFromServer(String cpr) {
		Gson gson = new Gson();
		String json = gson.toJson(new AddUpdateFavoritePatient("detteerennote",Color.WHITE)); 

		Log.d(TAG, "Json: "+json);
		HttpClient client = new DefaultHttpClient();
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);
	    try {
	    	HttpDelete request = new HttpDelete("http://130.225.184.205:8082/pfpas-mobile/rest/favorites/"+cpr);

	        Log.d(TAG, "Authorization: "+authorization);
	        request.addHeader("Authorization","Basic "+authorization);
	        
	        HttpResponse response = client.execute(request);
	        int responseCode = response.getStatusLine().getStatusCode();
	        Log.d(TAG, Integer.toString(responseCode));
	        // handle response here...
	    }catch (Exception ex) {
	        // handle exception here
	    } finally {
	        client.getConnectionManager().shutdown();
	    }
	} 
}
