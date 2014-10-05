package adapter;

import pojo.Employe;
import wrapper.OrmliteCursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.stmt.PreparedQuery;

public class EmployeAdapter extends OrmliteCursorAdapter<Employe>{

	private LayoutInflater inflater;
	
	/**
	 * At the time instantiating constructor we would not have Cursor so
	 * simply passing NULL
	 * 
	 * 
	 * How are you getting PreparedQuery<T> ??
	 * Use dao.queryBuilder().prepare()
	 * 
	 */
	public EmployeAdapter(Context context, Cursor c,PreparedQuery<Employe> query) {
		super(context, c, query);
		inflater = LayoutInflater.from(context);
	}

	/**
	 * This bindView() get called from "OrmliteCursorAdapter"
	 * 
	 * Although CursorAdapter getView() has build in ViewHolder implementation but still
	 * we are using own over it:)
	 * 
	 */
	@Override
	public void bindView(View itemView, Context context, Employe employe) {
		ViewHolder holder =  (ViewHolder) itemView.getTag();
		holder.fname.setText(employe.getFname());
		holder.lname.setText(employe.getLname());
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = inflater.inflate(android.R.layout.simple_list_item_2,  null);
		ViewHolder holder = new ViewHolder();
		holder.fname = (TextView)view.findViewById(android.R.id.text1);
		holder.lname = (TextView)  view.findViewById(android.R.id.text2);
		view.setTag(holder);
		return view;
	}

	private class ViewHolder  {
		TextView fname;
		TextView lname;
	}	
}
