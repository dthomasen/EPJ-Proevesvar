package dk.iha.itsmap.dpn.epjproevesvar.screens;

import dk.iha.itsmap.dpn.epjproevesvar.R;
import dk.iha.itsmap.dpn.epjproevesvar.business.PatientsFilterView;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class PatientOverview extends Activity {

	private final static String TAG="PatientsOverview";
	private TextView name;
	private TextView cpr;
	private TextView color;
	private TextView notificationMode;
	private BroadcastReceiver updateReciever;
	private String authorization;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_overview);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		authorization = getIntent().getExtras().getString("Authorization");
		
		Bundle bundle = getIntent().getExtras();
		Favorite choosen = (Favorite) bundle.get("ChoosenPatient");
		
		name = (TextView) findViewById(R.id.NameOutput);
		cpr = (TextView) findViewById(R.id.CPROutput);
		color = (TextView) findViewById(R.id.ColorOutput);
		notificationMode = (TextView) findViewById(R.id.NotificationOutput);
		
		name.setText(choosen.getName());
		cpr.setText(choosen.getCpr());
		color.setText(choosen.getColor());
		notificationMode.setText(choosen.getNotificationMode());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_patient_overview, menu);
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
//			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
