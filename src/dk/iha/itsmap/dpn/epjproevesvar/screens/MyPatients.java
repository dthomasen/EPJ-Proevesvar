package dk.iha.itsmap.dpn.epjproevesvar.screens;

import java.util.ArrayList;
import java.util.HashMap;

import dk.iha.itsmap.dpn.epjproevesvar.R;
import dk.iha.itsmap.dpn.epjproevesvar.business.PatientsFilterView;
import dk.iha.itsmap.dpn.epjproevesvar.services.FavoritesDownloadServices;
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
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyPatients extends ListActivity implements OnItemClickListener, OnClickListener, TextWatcher{

	private static final String TAG="MyPatients";
    private BroadcastReceiver updateReciever;
	private Favorite[] favorites;
	private FavoritesDownloadServices getFavoritesService;
	private ArrayList<String> favoriteNames;
	private HashMap<String, Favorite> favoriteMap;
	private ArrayAdapter<String> adapter;
	private ListView favoritesList;
	private boolean mBound = false;
	private PatientsFilterView filterInput;
	private String authorization;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_patients);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		authorization = getIntent().getExtras().getString("Authorization");
		favoritesList = getListView();
		favoriteNames = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, R.layout.customlistview, favoriteNames);
		filterInput = (PatientsFilterView) findViewById(R.id.FilterText);
		filterInput.addTextChangedListener(this);
		favoritesList.setAdapter(adapter);
		favoritesList.setOnItemClickListener(this);
		updateReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG,"FavoritesUpdated broadcast recieved");
                favorites = getFavoritesService.getFavorites();
                favoriteNames.clear();
                for(Favorite s : favorites){
                	favoriteNames.add(s.getName());
                }
                favoriteMap = getFavoritesService.getFavoritesMap();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_patients, menu);
		return true;
	}

	public void onPause() {
	     super.onPause();
	     Log.d(TAG,"OnPause called");
	     overridePendingTransition(0, 0);
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
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		MyPatients.this.adapter.getFilter().filter(s);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Log.d(TAG,"StationsListItem clicked");
		Favorite choosenFavorites = favoriteMap.get(((TextView) view).getText());
		
		Intent i = new Intent(this, PatientOverview.class);
		i.putExtra("ChoosenPatient", choosenFavorites);
		startActivityForResult(i, 0);
//  		startActivity(i); 
	}
}
