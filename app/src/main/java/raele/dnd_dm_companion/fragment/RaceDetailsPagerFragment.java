package raele.dnd_dm_companion.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;

/**
 * Created by lpr on 23/09/15.
 */
public class RaceDetailsPagerFragment extends Fragment {

    public static final String ARG_SHOWN_RACE = "SHOWN_RACE";

    private static final String GET_ALL_SUB_RACE_NAMES =
            "SELECT _id FROM _sub_race";

    private Integer[] mRaceIds;
    private int mInitialPage = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = -1;
        if (getArguments() != null && getArguments().containsKey(ARG_SHOWN_RACE)) {
            id = getArguments().getInt(ARG_SHOWN_RACE);
        }

        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_ALL_SUB_RACE_NAMES, null);

        mRaceIds = new Integer[cursor.getCount()];
        for (int i = 0; cursor.moveToNext(); i++) {
            mRaceIds[i] = cursor.getInt(0);

            if (id == mRaceIds[i]) {
                mInitialPage = i;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_race_details_pager, container, false);

        PagerAdapter adapter = new RacesPagerAdapter();
        ViewPager pager = (ViewPager) view.findViewById(R.id.phb_races_details_pager);
        pager.setAdapter(adapter);

        if (mInitialPage != -1) {
            pager.setCurrentItem(mInitialPage);
        }

        return view;
    }

    private class RacesPagerAdapter extends FragmentStatePagerAdapter {

        public RacesPagerAdapter() {
            super(getActivity().getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            RaceDetailsFragment fragment = new RaceDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(RaceDetailsFragment.ARG_RACE_ID, mRaceIds[position]);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mRaceIds.length;
        }

    }
}
