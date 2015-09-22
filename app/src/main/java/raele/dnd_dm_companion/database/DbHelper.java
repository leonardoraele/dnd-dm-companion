package raele.dnd_dm_companion.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import raele.util.android.log.Log;

/**
 * Created by lpr on 21/09/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String SETUP_SCRIPT_FILENAME = "database_setup.sql";
    private static final String DATABASE_FILENAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;

    public DbHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.begin();

        Log.info("Loading database setup sql file...");
        String[] sqlStatements;
        try {
            InputStream input = mContext.getAssets().open(SETUP_SCRIPT_FILENAME);
            StringWriter writer = new StringWriter();
            IOUtils.copy(input, writer);
            sqlStatements = writer.toString().split(";");
        } catch (IOException e) {
            Log.printStackTrace(e);
            return;
        }

        Log.info("Setting up database...");
        for (int i = 0; i < sqlStatements.length - 1; i++) {
            db.execSQL(sqlStatements[i]);
        }
//        db.execSQL(setupSql);

        Log.info("SQL setup file '" + SETUP_SCRIPT_FILENAME + "' was executed.");
        Log.end();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.begin();
        Log.info("Updating database from version " + oldVersion + " to " + newVersion);

        Log.info("(TODO) Calling the initial database setup as this is a stub, not implemented, method.");
        onCreate(db); // TODO

        Log.end();
    }

}
