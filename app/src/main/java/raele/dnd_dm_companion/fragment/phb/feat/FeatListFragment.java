package raele.dnd_dm_companion.fragment.phb.feat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;
import raele.util.android.Utils;
import raele.util.android.log.Log;

/**
 * Created by lpr on 18/11/15.
 */
public class FeatListFragment extends ListFragment {

    private static final String GET_ALL_FEATS =
            "SELECT tr_name._text, tr_description._text, tr_prerequisite._text " +
            "FROM _feature, _feat, _translation tr_name, _translation tr_description, _translation tr_prerequisite " +
            "WHERE _feat._feature_id = _feature._id AND " +
                    "_feature._name_id = tr_name._id AND tr_name._language = ? AND " +
                    "_feature._description_id = tr_description._id AND tr_description._language = ? AND " +
                    "_feat._prerequisite_id = tr_prerequisite._id AND tr_prerequisite._language = ?";

    private Feat[] mFeats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.begin();

        String lg = Locale.getDefault().getISO3Language().toUpperCase();
        Log.info("Using language " + lg + ".");

        Log.info("Requesting a readable database...");
        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

        Log.info("Querying for all feats...");
        Cursor cursor = db.rawQuery(GET_ALL_FEATS, new String[]{lg, lg, lg});

        Log.info("Found " + cursor.getCount() + " results.");

        Log.info("Setting up the list adapter...");
        List<HashMap<String, Object>> data = new ArrayList<>(cursor.getCount());
        mFeats = new Feat[cursor.getCount()];
        for (int i = 0; cursor.moveToNext(); i++) {
            Feat feat = new Feat();
            feat.setName(cursor.getString(0));
            feat.setDescription(cursor.getString(1));
            feat.setPrerequisite(cursor.getString(2));
            mFeats[i] = feat;

            HashMap<String, Object> rowData = new HashMap<>(1);
            rowData.put("name", feat.getName());
            data.add(rowData);
        }

        String[] from = new String[]{"name"};

        int[] to = new int[]{android.R.id.text1};

        setListAdapter(new SimpleAdapter(getActivity(), data, android.R.layout.simple_list_item_1, from, to));

        Log.info("Closing cursor...");
        cursor.close();

        Log.end();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Utils.of(getActivity()).showInfoDialog(
                mFeats[position].getName(),
                "" + getString(R.string.prerequisite) + ": " + mFeats[position].getPrerequisite() +
                        '\n' + mFeats[position].getDescription()
        );
    }

    private class Feat {
        private String name;
        private String description;
        private String prerequisite;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setPrerequisite(String prerequisite) {
            this.prerequisite = prerequisite;
        }

        public String getPrerequisite() {
            return prerequisite;
        }
    }
}
