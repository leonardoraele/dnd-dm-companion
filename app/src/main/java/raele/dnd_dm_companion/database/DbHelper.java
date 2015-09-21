package raele.dnd_dm_companion.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.activeandroid.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by lpr on 21/09/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String SETUP_SCRIPT_FILENAME = "database_setup.sql";

    private final Context mContext;

    public DbHelper(Context context, String nameId, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nameId, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            InputStream input = mContext.getAssets().open(SETUP_SCRIPT_FILENAME);
            String sql = IOUtils.toString(input, encoding);
            String theString = writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db); // TODO
    }

}
