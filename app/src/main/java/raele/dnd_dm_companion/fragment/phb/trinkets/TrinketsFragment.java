package raele.dnd_dm_companion.fragment.phb.trinkets;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;
import raele.util.android.log.Log;

/**
 * Created by lpr on 19/10/15.
 */
public class TrinketsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.begin();

        Log.info("Requesting database...");
        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

        // TODO Request trinkets tables...

        Log.end();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trinkets, container, false);

        // TODO Setup list R.id.phb_trinkets_list with trinkets tables...
        // TODO Each entry of the list must be clickable to see the full list but also must be a button to quick roll on the table. At a click, a popup dialog shows with the result.
        // TODO A button will also stay at the very bottom of this screen to quickly roll on a random table.
        // TODO The action bar must have a button to filter the lists. At default config, this button will be marked to only show official lists. (books and unearthed arcana)

        return view;
    }
}
