package raele.dnd_dm_companion.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;

/**
 * Created by lpr on 28/09/15.
 */
public class ClassesFragment extends Fragment {


    private static final String GET_ALL_CLASSES_SQL =
            "SELECT _class._id, name._text " +
            "FROM _class, _translation name " +
            "WHERE _class._name_id = name._id AND " +
                    "name._language = ?";

    public class Class {
        public int id;
        public String name;
        public Class(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private ArrayList<Class> mClasses;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String language = Locale.getDefault().getISO3Language().toUpperCase();

        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_ALL_CLASSES_SQL, new String[] {language});

        mClasses = new ArrayList<Class>(cursor.getCount());
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            mClasses.add(new Class(id, name));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classes, container, false);

        ListView list = (ListView) view.findViewById(R.id.phb_classes_menu_list);
        final ClassListAdapter adapter = new ClassListAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class clazz = adapter.getItem(position);
                selectClass(clazz);
            }
        });

        return view;
    }

    private class ClassListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mClasses.size();
        }

        @Override
        public Class getItem(int position) {
            return mClasses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getActivity().getLayoutInflater().inflate(
                    R.layout.layout_phb_classes_menu_item, (ViewGroup) convertView, false);

            Class clazz = getItem(position);
            TextView nameView = (TextView) view.findViewById(R.id.phb_classes_menu_list_item_name);
            nameView.setText(clazz.name);

            return view;
        }
    }

    public void selectClass(Class clazz) {
        ClassDetailsFragment fragment = new ClassDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ClassDetailsFragment.ARGS_CLASS_ID, clazz.id);
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }
}
