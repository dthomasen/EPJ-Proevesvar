package dk.iha.itsmap.dpn.epjproevesvar.screens;

import dk.iha.itsmap.dpn.epjproevesvar.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ManageFavorites extends Activity implements android.view.View.OnClickListener{

	private Button addNewFavorite;
	private String authorization;
	private Button removeFavorite;
	private static final String TAG="ManageFavorites";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_favorites);
		authorization = getIntent().getExtras().getString("Authorization");
		addNewFavorite = (Button) findViewById(R.id.addnewfavoritebutton);
		addNewFavorite.setOnClickListener(this);
		removeFavorite = (Button) findViewById(R.id.removefavoritebutton);
		removeFavorite.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_patient_information, menu);
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
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addnewfavoritebutton:
			Log.d(TAG,"Add new favorite Button Clicked");
			Intent intent = new Intent(this, AddFavorite.class);
			intent.putExtra("Authorization", authorization);
			startActivity(intent);
			finish();
			break;
		case R.id.removefavoritebutton:
			Log.d(TAG,"remove favorites Button Clicked");
			Intent intent2 = new Intent(this, RemoveFavorite.class);
			intent2.putExtra("Authorization", authorization);
			startActivity(intent2);
			finish();
			break;
		}
	}
}
