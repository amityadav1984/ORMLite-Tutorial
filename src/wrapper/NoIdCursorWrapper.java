package wrapper;

import java.sql.SQLException;

import com.j256.ormlite.stmt.PreparedQuery;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.widget.ListView;

/**
 * A {@link CursorWrapper} implementation that allows a {@link Cursor} without a
 * field named "_id" to be used with various Android {@link ListView} classes
 * that expect a column named "_id". This is done by specifying an alias field
 * name to be used in place of "_id".
 * 
 * 
 * 
 * CustomBaseDaoImpl<T, ID> used this class in 
 * 
 * public Cursor getCursor(PreparedQuery<T> query) throws SQLException
 * {
 * 		NoIdCursorWrapper wrapper = new NoIdCursorWrapper(base, idColumnIndex);
 * }
 */
public class NoIdCursorWrapper extends CursorWrapper
{
    private int idColumnIndex;

    /**
     * Create a NoIdCursorWrapper using the alias column index.
     * 
     * @param c
     *            the cursor to wrap
     * @param idColumnIndex
     *            the column index to use as the _id column alias
     */
    public NoIdCursorWrapper(Cursor c, int idColumnIndex)
    {
        super(c);
        this.idColumnIndex = idColumnIndex;
    }

    /**
     * Create a NoIdCursorWrapper using the alias column name.
     * 
     * @param c
     *            the cursor to wrap
     * @param idColumnName
     *            the column name to use as the _id column alias
     */
    public NoIdCursorWrapper(Cursor c, String idColumnName)
    {
        super(c);
        idColumnIndex = c.getColumnIndex(idColumnName);
    }

    @Override
    public int getColumnIndex(String columnName)
    {
        int index = super.getColumnIndex(columnName);
        if (index < 0 && "_id".equals(columnName))
        {
            index = idColumnIndex;
        }
        return index;
    };

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException
    {
        int index = getColumnIndex(columnName);
        if (index >= 0)
        {
            return index;
        }
        // let the AbstractCursor generate the exception
        return super.getColumnIndexOrThrow(columnName);
    };
}