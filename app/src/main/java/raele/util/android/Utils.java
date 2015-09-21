package raele.util.android;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

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
}
