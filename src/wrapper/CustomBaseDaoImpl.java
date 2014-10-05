package wrapper;

import java.sql.SQLException;

import android.database.Cursor;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfig;

/**
 * 
 * Why using CustomBaseDaoImpl<T, ID> ?
 * Just to get getCursor() method which is not the part of DAO<T,ID> interface
 * define getCursor() method in "CustomDao<T, ID>" and implement in "CustomBaseDaoImpl<T, ID>"
 * 
 * 
 * 
 * Why CusomDao<T, ID> ?? As we can directly define getCursor() in CustomBaseDaoImpl<T, ID>
 * Gray Watson: If you want a better class hierarchy or if you need to add additional methods to your DAOs, 
 * you should consider defining an interface which extends the Dao interface. 
 * The interface isn’t required but it is a good pattern so your code is less tied to JDBC for persistence.
 * 
 * 
 * 
 * How this class integrated with LIFE_CYCLE
 * In POJO class use daoClass = CustomBaseDaoImpl.class
 * and in "DatabaseHelper" getDao() method return type is CustomDao<T, ID>
 * getDao() internally return "CustomBaseDaoImpl" because of daoClass = CustomBaseDaoImpl.class is defined in POJO
 * which is eventually assign to CustomDao<T, ID> with concept of inheritance that
 * BaseClass pointer can point to DerivedClass
 * CustomBaseDaoImpl<T, ID> extends BaseDaoImpl<T, ID> implements CustomDao<T, ID>
 * 
 * 
 * 
 * getDao() return type requirement is <D extends Dao<T, ?>, T>
 * mean a Class which extends/implements Dao<T, ID> then why unnecessary extending BaseDaoImpl<T, ID> ??
 * Dao<T, ID> is an interface and BaseDaoImpl<T, ID> in a concrete implementation of Dao<T, ID>
 * if you don't extends BaseDaoImpl<T, ID> then you have to implement all of required method of Dao<T, ID>
 * 
 * 
 * 
 * public abstract class CustomBaseDaoImpl<T, ID> extends BaseDaoImpl<T, ID> implements CustomDao<T, ID>
 * removed abstract
 * To Fixed: java.sql.SQLException: Could not call the constructor in class class com.ormlite.tutorial.CustomBaseDaoImpl
 * 
 */
//public class CustomBaseDaoImpl<T, ID> extends BaseDaoImpl<T, ID> implements CustomDao<T, ID>
public class CustomBaseDaoImpl<T, ID> extends BaseDaoImpl<T, ID> implements CustomDao<T, ID>
{

    public CustomBaseDaoImpl(Class<T> dataClass) throws SQLException
    {
        super(dataClass);
    }

    public CustomBaseDaoImpl(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }

    public CustomBaseDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException
    {
        super(connectionSource, tableConfig);
    }

    /**
     * The only class which having method to return "android.database.Cursor" is "AndroidCompiledStatement" 
     * via getCurssor() method 
     */
    public Cursor getCursor(PreparedQuery<T> query) throws SQLException
    {
        DatabaseConnection readOnlyConn = connectionSource.getReadOnlyConnection();
        AndroidCompiledStatement stmt = (AndroidCompiledStatement) query.compile(readOnlyConn, StatementBuilder.StatementType.SELECT);
        Cursor base =  stmt.getCursor();
        String idColumnName = getTableInfo().getIdField().getColumnName();
        int idColumnIndex = base.getColumnIndex(idColumnName);
        NoIdCursorWrapper wrapper = new NoIdCursorWrapper(base, idColumnIndex);
        return wrapper;
    }
    
/*    public Loader<List<T>> getResultSetLoader(Context context, PreparedQuery<T> query) throws SQLException
    {
        return new OrmliteListLoader<T, ID>(context, this, query);
    }*/
    
/*    public OrmliteCursorLoader<T> getSQLCursorLoader(Context context, PreparedQuery<T> query) throws SQLException
    {
        return new OrmliteCursorLoader<T>(context, this, query);
    }*/
}

