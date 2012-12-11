package dk.iha.itsmap.dpn.epjproevesvar.screens;

import dk.iha.itsmap.dpn.epjproevesvar.R;
import dk.iha.itsmap.dpn.epjproevesvar.business.Analyses;
import dk.iha.itsmap.dpn.epjproevesvar.business.Favorite;
import dk.iha.itsmap.dpn.epjproevesvar.business.HistoricValues;
import dk.iha.itsmap.dpn.epjproevesvar.business.LabResult;
import dk.iha.itsmap.dpn.epjproevesvar.business.NextRequisition;
import dk.iha.itsmap.dpn.epjproevesvar.business.PatientsFilterView;
import dk.iha.itsmap.dpn.epjproevesvar.business.UAnalyses;
import dk.iha.itsmap.dpn.epjproevesvar.services.FavoritesDownloadServices;
import dk.iha.itsmap.dpn.epjproevesvar.services.LabResultDownloadService;
import dk.iha.itsmap.dpn.epjproevesvar.services.FavoritesDownloadServices.GetFavoritesBinder;
import dk.iha.itsmap.dpn.epjproevesvar.services.LabResultDownloadService.GetLabResultBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.widget.LinearLayout;

public class PatientOverview extends Activity {

	private final static String TAG="PatientsOverview";
	private TextView name;
	private TextView cpr;
	private TextView color;
	private TextView notificationMode;
	private BroadcastReceiver updateLabResultReciever;
	private String authorization;
	private LabResultDownloadService getLabResultService;
	protected LabResult labResults;
	protected boolean mBound;
	private String cprNumber;
	private LinearLayout latestLabResultLayout;
	private LinearLayout upcommingLabResultLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_overview);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d(TAG,getIntent().getExtras().getString("Authorization"));
		authorization = getIntent().getExtras().getString("Authorization");
		latestLabResultLayout = (LinearLayout)findViewById(R.id.LatestResultLayout);
		upcommingLabResultLayout = (LinearLayout)findViewById(R.id.UpcommingResultLayout);
		updateLabResultReciever = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				labResults = getLabResultService.getLabResults();
				Log.d(TAG,"LabResultsUpdated broadcast recieved");
				updateLabResultViews(labResults);
			}
		};

		this.registerReceiver(updateLabResultReciever, new IntentFilter("LabResultsUpdated"));

		Bundle bundle = getIntent().getExtras();
		Favorite choosen = (Favorite) bundle.get("ChoosenPatient");
		getActionBar().setTitle(choosen.getName());
		name = (TextView) findViewById(R.id.NameOutput);
		cpr = (TextView) findViewById(R.id.CPROutput);

		color = (TextView) findViewById(R.id.ColorOutput);
		notificationMode = (TextView) findViewById(R.id.NotificationOutput);

		name.setText(choosen.getName());
		cprNumber = choosen.getCpr();
		cpr.setText(choosen.getCpr());
		color.setText(choosen.getColor());
		notificationMode.setText(choosen.getNotificationMode());
	}

	private void updateLabResultViews(LabResult labresults){
		if(labresults.getAnswers().size() == 0){
			TextView noAnswers = new TextView(this);
			noAnswers.setText("No lab results");
			noAnswers.setId(1);
			noAnswers.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			noAnswers.setPadding(0, 10, 0, 0);
			noAnswers.setGravity(Gravity.CENTER_HORIZONTAL);
			latestLabResultLayout.addView(noAnswers);
		}else{
			//Latest lab results
			Analyses latest = labresults.getAnswers().get(0).getAnalyses().get(0);

			//Sampling time
			TextView samplingTime = new TextView(this);
			samplingTime.setText(labresults.getAnswers().get(0).getSamplingTime());
			samplingTime.setId(1);
			samplingTime.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			samplingTime.setPadding(0, 10, 0, 0);
			samplingTime.setGravity(Gravity.CENTER_HORIZONTAL);
			latestLabResultLayout.addView(samplingTime);

			TextView analysisName = new TextView(this);
			analysisName.setText(latest.getAnalysisName());
			analysisName.setId(1);
			analysisName.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			analysisName.setGravity(Gravity.CENTER_HORIZONTAL);
			latestLabResultLayout.addView(analysisName);

			TextView value = new TextView(this);
			value.setText("Value: "+latest.getValue());
			value.setId(1);
			value.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			value.setGravity(Gravity.CENTER_HORIZONTAL);
			if(latest.getOutsideNormalRange()){
				value.setTextColor(Color.RED);
			}
			latestLabResultLayout.addView(value);

			TextView min = new TextView(this);
			min.setText("Min: "+latest.getMin());
			min.setId(1);
			min.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			min.setGravity(Gravity.CENTER_HORIZONTAL);
			latestLabResultLayout.addView(min);

			TextView max = new TextView(this);
			max.setText("Max: "+latest.getMax());
			max.setId(1);
			max.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			max.setGravity(Gravity.CENTER_HORIZONTAL);
			latestLabResultLayout.addView(max);

			TextView historicValues = new TextView(this);
			historicValues.setText("Previous values");
			historicValues.setId(1);
			historicValues.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			historicValues.setPadding(0, 10, 0, 0);
			historicValues.setGravity(Gravity.CENTER_HORIZONTAL);
			historicValues.setTypeface(null,Typeface.BOLD);
			historicValues.setPadding(0, 10, 0, 0);
			historicValues.setTextSize(18);
			latestLabResultLayout.addView(historicValues);

			for(int i=0; i<latest.getHistoricValues().size(); i++){
				HistoricValues current = latest.getHistoricValues().get(i);
				TextView samplingtimeH = new TextView(this);
				samplingtimeH.setText(current.getSamplingTime());
				samplingtimeH.setId(1);
				samplingtimeH.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				samplingtimeH.setGravity(Gravity.CENTER_HORIZONTAL);
				samplingtimeH.setTextSize(15);
				samplingtimeH.setPadding(0,10, 0, 0);
				latestLabResultLayout.addView(samplingtimeH);

				TextView samplingValue = new TextView(this);
				samplingValue.setText(current.getValue());
				samplingValue.setId(1);
				samplingValue.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				samplingValue.setGravity(Gravity.CENTER_HORIZONTAL);
				if(latest.getOutsideNormalRange()){
					samplingValue.setTextColor(Color.RED);
				}else{
					samplingValue.setTextColor(Color.rgb(0, 102, 0));
				}
				samplingValue.setTextSize(18);
				latestLabResultLayout.addView(samplingValue);
			}
		}
		//Upcomming lab results
		if(labresults.getNextRequisition() == null){
			TextView noUpcomming = new TextView(this);
			noUpcomming.setText("No upcomming lab results");
			noUpcomming.setId(1);
			noUpcomming.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			noUpcomming.setPadding(0, 10, 0, 0);
			noUpcomming.setGravity(Gravity.CENTER_HORIZONTAL);
			upcommingLabResultLayout.addView(noUpcomming);
		}else{
			NextRequisition upcommingLabResults = labresults.getNextRequisition();
			TextView samplingTimeU = new TextView(this);
			samplingTimeU.setText(upcommingLabResults.getSamplingTime());
			samplingTimeU.setId(1);
			samplingTimeU.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			samplingTimeU.setPadding(0, 10, 0, 0);
			samplingTimeU.setGravity(Gravity.CENTER_HORIZONTAL);
			latestLabResultLayout.addView(samplingTimeU);

			for(int j=0; j<upcommingLabResults.getAnalyses().size(); j++){
				UAnalyses currentU = upcommingLabResults.getAnalyses().get(j);

				TextView UanalysisName = new TextView(this);
				UanalysisName.setText(currentU.getAnalysisName());
				UanalysisName.setId(1);
				UanalysisName.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				UanalysisName.setGravity(Gravity.CENTER_HORIZONTAL);
				UanalysisName.setTextSize(15);
				UanalysisName.setPadding(0,10, 0, 0);

				TextView UanalysisPriority = new TextView(this);
				UanalysisPriority.setText(currentU.getPriority());
				UanalysisPriority.setId(1);
				UanalysisPriority.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				UanalysisPriority.setGravity(Gravity.CENTER_HORIZONTAL);

				UanalysisPriority.setTextSize(18);

				if(currentU.getPriority().equals("NORMAL")){
					UanalysisPriority.setTextColor(Color.rgb(0, 102, 0));
				}

				upcommingLabResultLayout.addView(UanalysisName);
				upcommingLabResultLayout.addView(UanalysisPriority);
			}
		}
	}

	@Override
	protected void onStart(){
		super.onStart();
		Log.d(TAG,"OnStart called");
		Intent intent = new Intent(this, LabResultDownloadService.class);
		bindService(intent, connectionToService, Context.BIND_AUTO_CREATE);
		this.registerReceiver(updateLabResultReciever, new IntentFilter("LabResultsUpdated"));
	}

	private ServiceConnection connectionToService = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
				IBinder service) {
			GetLabResultBinder binder = (GetLabResultBinder) service;
			getLabResultService = binder.getService();
			mBound = true;
			getLabResultService.updateLabResults(authorization,cprNumber);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

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

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"OnStop called");
		if (mBound) {
			unbindService(connectionToService);
			mBound = false;
		}
		unregisterReceiver(updateLabResultReciever);
	}

	@Override
	protected void onResume() {
		super.onStop();
		Log.d(TAG,"OnResume called");
		Intent intent = new Intent(this, LabResultDownloadService.class);
		bindService(intent, connectionToService, Context.BIND_AUTO_CREATE);
		this.registerReceiver(updateLabResultReciever, new IntentFilter("LabResultsUpdated"));
	}

	public void onPause() {
		super.onPause();
		Log.d(TAG,"OnPause called");
		overridePendingTransition(0, 0);
		if (mBound) {
			unbindService(connectionToService);
			mBound = false;
		}
	}
}
