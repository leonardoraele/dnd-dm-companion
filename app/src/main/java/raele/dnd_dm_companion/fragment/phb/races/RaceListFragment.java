package raele.dnd_dm_companion.fragment.phb.races;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Locale;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;
import raele.util.android.log.Log;

/**
 * Created by lpr on 21/09/15.
 */
public class RaceListFragment extends Fragment {

    private static final String GET_ALL_SUB_RACE_NAMES =
            "SELECT sr._id, tr._text FROM _sub_race sr, _translation tr WHERE " +
                    "sr._name_id = tr._id and tr._language = ?";

    private Integer[] mRaceIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.begin();
        View view = inflater.inflate(R.layout.fragment_races, container, false);

        ListView listView = (ListView) view.findViewById(R.id.phb_races_menu_list);
        ListAdapter adapter = createRacesListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        Log.end();
        return view;
    }

    private ArrayAdapter<String> createRacesListAdapter() {
        Log.begin();

        String language = Locale.getDefault().getISO3Language().toUpperCase();
        Log.info("Using language: " + language);

        Log.info("Requesting the database...");
        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

        Log.info("Querying the database for races...");
        Cursor cursor = db.rawQuery(GET_ALL_SUB_RACE_NAMES, new String[] {language});

        Log.info("Query returned " + cursor.getCount() + " entries.");
        String[] raceNames = new String[cursor.getCount()];
        mRaceIds = new Integer[cursor.getCount()];
        for (int i = 0; cursor.moveToNext(); i++) {
            mRaceIds[i] = cursor.getInt(0);
            raceNames[i] = cursor.getString(1);
        }
        Log.info(Arrays.toString(raceNames));

        Log.info("Now building the adapter...");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_phb_races_menu_item,
                R.id.phb_races_menu_item_name, raceNames);

        Log.info("Finished.");
        Log.end();
        return adapter;
    }

    public void selectItem(int position) throws IndexOutOfBoundsException {
        RaceDetailsPagerFragment fragment = new RaceDetailsPagerFragment();
        Bundle args = new Bundle();
        args.putInt(RaceDetailsPagerFragment.ARG_SHOWN_RACE, mRaceIds[position]);
        fragment.setArguments(args);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

}
