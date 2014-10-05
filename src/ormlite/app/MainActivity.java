package ormlite.app;

import java.sql.SQLException;

import pojo.Employe;
import wrapper.CustomDao;
import wrapper.OrmliteCursorLoader;
import adapter.EmployeAdapter;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import database.DatabaseHelper;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

	private DatabaseHelper dbHelper;
//	private RuntimeExceptionDao<Employe, String> simpleRuntimeDao;
//	private Dao<Employe, String> simpleDao;
	private CustomDao<Employe, String> customDao;
	EmployeAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dbHelper = new DatabaseHelper(getApplicationContext());
		//simpleRuntimeDao = dbHelper.getSimpleRuntimeDao();
		//simpleDao = dbHelper.getSimpleDao();
		customDao = dbHelper.getCustomDao();
		addRow();
		
		ListView list = (ListView) findViewById(android.R.id.list);
		try {
			//adapter = new EmployeAdapter(this, null, simpleRuntimeDao.queryBuilder().prepare());
			//adapter = new EmployeAdapter(this, null, simpleDao.queryBuilder().prepare());
			
			/**
			 * This time we don't have Cursor
			 */
			adapter = new EmployeAdapter(this, null, customDao.queryBuilder().prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		list.setAdapter(adapter);
		
		
		/**
		 * initialize CursorLoader
		 */
		getLoaderManager().initLoader(201, null, this);
	}

	private String[][] data = {{"Gray", "Watson"}, {"Ravi", "Verma"}, {"Ashok", "Singhal"},{"Amit", "Yadav"}, {"Jake", "Wharton"}};
	public void addRow(){

		for(int i=0; i < 5; i++){
			Employe employe = new Employe();
			employe.setFname(data[i][0]);
			employe.setLname(data[i][1]);
			dbHelper.addRow(employe);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		try {
			//return new OrmliteCursorLoader<Employe>(this, simpleRuntimeDao, simpleRuntimeDao.queryBuilder().prepare());
			
			/**
			 *  called by initLoader()
			 *  constructing CursorLaoder
			 *  Query is very important. Our result is based on this query
			 */
			return new OrmliteCursorLoader<Employe>(this, customDao, customDao.queryBuilder().prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	}

	
	/**
	 * Finally Loader will provide the Cursor (!NULL)
	 * swap this cursor and view the result...:)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		CursorAdapter _adapter = (CursorAdapter) adapter;
		if(_adapter != null && cursor != null){
			_adapter.swapCursor(cursor);
		}	
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		CursorAdapter _adapter = (CursorAdapter) adapter;
		if(_adapter != null){
			_adapter.swapCursor(null);
		}		
	}
}
