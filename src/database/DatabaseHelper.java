package database;

import java.sql.SQLException;

import pojo.Employe;
import wrapper.CustomDao;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "employe.sqlite";
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * OK if we are not going to add any custom method say, getCursor()
	 */
	private Dao<Employe, String> simpleDao = null;
	
	/**
	 * We know that getDao<T> is going to return CustomBaseDaoImpl<I, ID> since 
	 * in POJO we used daoClass = CustomBaseDaoImpl.class but we are taking CustomDao<T, ID> field
	 * as we know CustomBaseDaoImpl<T, ID> implements CustomDao<T, ID> and As per inheritance BaseClass
	 * pointer can point to DerivedClass :)
	 */
	private CustomDao<Employe, String> customDao = null;
	
	/**
	 * To get rid of try{}catch{} guard but can't use with CustomDAO since
	 * BasDaoImpl<T, ID> implementing Dao<T, ID>
	 * and CustomBaseDaoImpl<T, ID> has to extends BaseDaoImpl<T, ID>
	 * and all we know Java doen't support multiple inheritance
	 */
	private RuntimeExceptionDao<Employe, String> simpleRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		try {
			
			/**
			 * No API to drop the Database instead we can drop the table
			 */
			TableUtils.dropTable(connectionSource, Employe.class, true);
			
			
			/**
			 * And create it again
			 */
			TableUtils.createTable(connectionSource, Employe.class);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource ConnectionSource) {
		try {
			TableUtils.createTable(connectionSource, Employe.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource ConnectionSource, int oldVersion, int newVersion) {

	}

	public Dao<Employe, String> getSimpleDao(){
		if (simpleDao == null) {
			try {
				simpleDao = getDao(Employe.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return simpleDao;
	}	
	
	 
	/**
	 * 
	 * SOURCE CODE
	 * 
	 * public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
	 * //special reflection fu is now handled internally by create dao calling the database type
	 * Dao<T, ?> dao = DaoManager.createDao(getConnectionSource(), clazz);
	 * @SuppressWarnings("unchecked")
	 * D castDao = (D) dao;
	 * return castDao;
	 * 
	 */
	public CustomDao<Employe, String> getCustomDao(){
		if (customDao == null) {
			try {
				customDao = getDao(Employe.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return customDao;
	}
	
	public RuntimeExceptionDao<Employe, String> getSimpleRuntimeDao(){
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(Employe.class);
		}
		return simpleRuntimeDao;
	}	
	
	public void addRow(Employe employe){
		try {
			customDao.create(employe);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
