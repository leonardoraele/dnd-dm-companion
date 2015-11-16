package raele.util.android;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import raele.util.android.log.Log;

/**
 * Created by lpr on 21/09/15.
 */
public class Utils {

    private static final Map<Context, Utils> sInstances = new HashMap<>(1);

    private final Context mContext;

    public Utils(Context context) {
        mContext = context;
    }

    public static synchronized Utils of(Context context) {
        Utils result = sInstances.get(context);
        if (result == null) {
            result = new Utils(context);
            sInstances.put(context, result);
        }
        return result;
    }

    public void showLongToast(int textId) {
        Toast.makeText(mContext, mContext.getString(textId), Toast.LENGTH_LONG).show();
    }

    public void showLongToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(int textId) {
        Toast.makeText(mContext, mContext.getString(textId), Toast.LENGTH_SHORT).show();
    }

    public void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public void notImplemented() {
        showShortToast("Not implemented yet.");
    }

    public void logTable(String sample_name, SQLiteDatabase db) {
        Log.begin();
        Cursor cursor = db.rawQuery("SELECT * FROM " + sample_name, null);

        for (int i = 0; cursor.moveToNext(); i++) {
            Log.begin();
            for (int j = 0; j < cursor.getColumnCount(); j++) {
                Log.info("Row " + i + " column " + j + " (" + cursor.getColumnName(j) + ": " + cursor.getString(j));
            }
            Log.end();
        }
        Log.end();
    }

    public AlertDialog showInfoDialog(String title, String description) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(description)
                .create();
        dialog.show();
        return dialog;
    }
}
