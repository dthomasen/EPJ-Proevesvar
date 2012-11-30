package dk.iha.itsmap.dpn.epjproevesvar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener{
	private static final String TAG="MainMenu";
	private Button logoutButton;
	private Button myPatientsButton;
	private Button patientsInfoButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main_menu);
		logoutButton = (Button) findViewById(R.id.logout);
		myPatientsButton = (Button) findViewById(R.id.mypatients);
		patientsInfoButton = (Button) findViewById(R.id.patientsinformation);
		
		logoutButton.setOnClickListener(this);
		myPatientsButton.setOnClickListener(this);
		patientsInfoButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logout:
			Log.d(TAG,"Logout Button Clicked");
			break;
		case R.id.mypatients:
			Log.d(TAG,"My patients Button Clicked");
			Intent MyPatients = new Intent(this, MyPatients.class);
			MyPatients.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MyPatients.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			this.startActivity(MyPatients);
			break;
		case R.id.patientsinformation:
			Log.d(TAG,"Patients information Button Clicked");
			Intent PatientsInformation = new Intent(this, PatientInformation.class);
			PatientsInformation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PatientsInformation.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			this.startActivity(PatientsInformation);
			break;
		}
		
	}
}
