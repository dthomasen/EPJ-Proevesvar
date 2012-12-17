package dk.iha.itsmap.dpn.epjproevesvar.services;

import java.util.ArrayList;
import java.util.List;

import dk.iha.itsmap.dpn.epjproevesvar.R;
import dk.iha.itsmap.dpn.epjproevesvar.business.Favorite;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

public class PatientBaseAdapter extends BaseAdapter implements Filterable {

	private static ArrayList<Favorite> searchArrayList;
	private LayoutInflater mInflater;
	private static final String TAG="PatientBaseAdapter";
	 List<Favorite> arrayList;      
     List<Favorite> mOriginalValues; // Original Values
     LayoutInflater inflater;

	public PatientBaseAdapter(Context context, ArrayList<Favorite> results) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return searchArrayList.size();
	}

	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void sendList(ArrayList<Favorite> results){
		searchArrayList = results;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Log.d(TAG,"GETVIEW called!");
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.custom_patient_view, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.line_name);
			holder.txtCpr = (TextView) convertView
					.findViewById(R.id.line_cpr);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtName.setText(searchArrayList.get(position).getName());
		holder.txtCpr.setText(searchArrayList.get(position).getCpr());

		return convertView;
	}

	static class ViewHolder {
		TextView txtName;
		TextView txtCpr;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,FilterResults results) {
				searchArrayList = (ArrayList<Favorite>) results.values; // has the filtered values
				notifyDataSetChanged();  // notifies the data with new filtered values
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
				List<Favorite> FilteredArrList = new ArrayList<Favorite>();

				if (mOriginalValues == null) {
					mOriginalValues = new ArrayList<Favorite>(searchArrayList); // saves the original data in mOriginalValues
				}

				/********
				 * 
				 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
				 *  else does the Filtering and returns FilteredArrList(Filtered)  
				 *
				 ********/
				if (constraint == null || constraint.length() == 0) {

					// set the Original result to return  
					results.count = mOriginalValues.size();
					results.values = mOriginalValues;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < mOriginalValues.size(); i++) {
						String data = mOriginalValues.get(i).getName();
						if (data.toLowerCase().startsWith(constraint.toString())) {
							FilteredArrList.add(mOriginalValues.get(i));
						}
					}
					// set the Filtered result to return
					results.count = FilteredArrList.size();
					results.values = FilteredArrList;
				}
				return results;
			}
		};
		return filter;
	}
}
