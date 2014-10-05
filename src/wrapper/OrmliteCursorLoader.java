package wrapper;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.sql.SQLException;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

/**
 * A CursorLoader to load data backed by a ContentProvider. Alternatively, 
 * you can implement your own subclass of Loader or AsyncTaskLoader to load data from some other source.
 */
public class OrmliteCursorLoader<T> extends AsyncTaskLoader<Cursor> {
    final ForceLoadContentObserver mObserver;

    private Cursor mCursor;
    private CustomDao<T, ?> mDao;
    private PreparedQuery<T> mQuery;
    
    public OrmliteCursorLoader(Context context, CustomDao<T,?> dao, PreparedQuery<T> query) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mDao = dao;
        mQuery = query;
    }    

    
    /**
     * Here finally our precious getCursor() method is being used cheers!!!
     */
    
    /* Runs on a worker thread */
    @Override
    public Cursor loadInBackground() {
        Cursor cursor = null;
        try
        {
            cursor = mDao.getCursor(mQuery);
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
            registerContentObserver(cursor, mObserver);
        }
        return cursor;
    }

    /**
     * Registers an observer to get notifications from the content provider
     * when the cursor needs to be refreshed.
     */
    void registerContentObserver(Cursor cursor, ContentObserver observer) {
        cursor.registerContentObserver(mObserver);
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        
        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }

    

    public PreparedQuery<T> getQuery()
    {
        return mQuery;
    }

    public void setQuery(PreparedQuery<T> mQuery)
    {
        this.mQuery = mQuery;
    }

    public Dao<T, ?> getDao()
    {
        return mDao;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        writer.print(prefix); writer.print("mCursor="); writer.println(mCursor);
    }
}
