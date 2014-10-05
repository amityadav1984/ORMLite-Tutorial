package wrapper;

import java.sql.SQLException;

import android.database.Cursor;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

public interface CustomDao<T, ID> extends Dao<T, ID> {

	/**
	 * Define getCursor() here and implement in CustomBaseDaoImpl<T, ID>
	 * 
	 * Why CusomDao<T, ID> ?? As we can directly define getCursor() in CustomBaseDaoImpl<T, ID>
	 * Gray Watson: If you want a better class hierarchy or if you need to add additional methods to your DAOs, 
	 * you should consider defining an interface which extends the Dao interface. 
	 * The interface isn’t required but it is a good pattern so your code is less tied to JDBC for persistence.
	 * 
	 * 
	 */
	public Cursor getCursor(PreparedQuery<T> query) throws SQLException;
}
