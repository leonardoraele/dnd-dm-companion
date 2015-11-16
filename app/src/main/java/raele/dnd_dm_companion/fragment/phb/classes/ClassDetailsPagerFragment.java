package raele.dnd_dm_companion.fragment.phb.classes;

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
 * Created by lpr on 13/10/15.
 */
public class ClassDetailsPagerFragment extends Fragment {

    public static final String ARG_SHOWN_CLASS = "SHOWN_CLASS";

    private static final String GET_ALL_CLASS_NAMES =
            "SELECT _id FROM _class";

    private Integer[] mClassIds;
    private int mInitialPage = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = -1;
        if (getArguments() != null && getArguments().containsKey(ARG_SHOWN_CLASS)) {
            id = getArguments().getInt(ARG_SHOWN_CLASS);
        }

        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_ALL_CLASS_NAMES, null);

        mClassIds = new Integer[cursor.getCount()];
        for (int i = 0; cursor.moveToNext(); i++) {
            mClassIds[i] = cursor.getInt(0);

            if (id == mClassIds[i]) {
                mInitialPage = i;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_details_pager, container, false);

        PagerAdapter adapter = new ClassesPagerAdapter();
        ViewPager pager = (ViewPager) view.findViewById(R.id.phb_classes_details_pager);
        pager.setAdapter(adapter);

        if (mInitialPage != -1) {
            pager.setCurrentItem(mInitialPage);
        }

        return view;
    }

    private class ClassesPagerAdapter extends FragmentStatePagerAdapter {

        public ClassesPagerAdapter() {
            super(getActivity().getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            ClassDetailsFragment fragment = new ClassDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(ClassDetailsFragment.ARGS_CLASS_ID, mClassIds[position]);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mClassIds.length;
        }

    }

}
