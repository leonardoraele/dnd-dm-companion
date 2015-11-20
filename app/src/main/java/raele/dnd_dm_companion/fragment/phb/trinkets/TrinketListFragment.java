package raele.dnd_dm_companion.fragment.phb.trinkets;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;
import raele.util.android.Utils;
import raele.util.android.log.Log;

/**
 * Created by lpr on 19/10/15.
 */
public class TrinketListFragment extends Fragment {

    private static final String SQL_GET_ALL_TRINKET_TABLE =
            "SELECT _rollable_table._id, _rollable_table._roll, tr_name._text, tr_description._text " +
            "FROM _trinket_table, _rollable_table, _translation tr_name, _translation tr_description " +
            "WHERE _trinket_table._rollable_table_id = _rollable_table._id AND " +
                    "tr_name._id = _rollable_table._name_id AND tr_name._language = ? AND " +
                    "tr_description._id = _rollable_table._description_id AND tr_description._language = ?";

    private static final String SQL_GET_TABLE_ITEM =
            "SELECT tr_description._text " +
            "FROM _table_item, _translation tr_description " +
            "WHERE _table_item._rollable_table_id = ? AND " +
                    "_table_item._range_from >= ? AND " +
                    "_table_item._range_to <= ? AND " +
                    "tr_description._id = _table_item._description_id AND tr_description._language = ?";

    private ArrayList<TrinketTable> mTableList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.begin();

        Log.info("Requesting database...");
        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

        String lg = Locale.getDefault().getISO3Language().toUpperCase();
        Log.info("Using language " + lg + "...");

        Log.info("Querying for all trinkets table in database.");
        Cursor cursor = db.rawQuery(SQL_GET_ALL_TRINKET_TABLE, new String[]{lg, lg});
        Log.info("Query returned " + cursor.getCount() + " results.");

        mTableList = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            TrinketTable table = new TrinketTable();
            table.setId(cursor.getInt(0));
            table.setRoll(cursor.getString(1));
            table.setName(cursor.getString(2));
            table.setDescription(cursor.getString(3));
            mTableList.add(table);
            Log.info("Added table " + table);
        }

        db.close();

        Log.end();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.begin();

        View view = inflater.inflate(R.layout.fragment_trinkets, container, false);

        ListView list = (ListView) view.findViewById(R.id.phb_trinkets_list);
        ListAdapter adapter = createAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new TrinketTableFragment();
                Bundle args = new Bundle();
                args.putInt(TrinketTableFragment.ARG_TABLE_ID, mTableList.get(position).getId());
                fragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(toString())
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String tableDescription = mTableList.get(position).getDescription();
                Utils.of(getActivity()).showLongToast(tableDescription);
                return true;
            }
        });

        View randomRoll = view.findViewById(R.id.phb_trinkets_quick_roll);
        randomRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomIndex = new Random().nextInt(mTableList.size());
                TrinketTable table = mTableList.get(randomIndex);
                quickRoll(table);
            }
        });

        // TODO The action bar must have a button to filter the lists. At default config, this button will be marked to only show official lists. (books and unearthed arcana)

        Log.end();
        return view;
    }

    private ListAdapter createAdapter() {
        return new BaseAdapter() {
            @Override
            public int getCount() {
                return mTableList.size();
            }

            @Override
            public TrinketTable getItem(int position) {
                return mTableList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return getItem(position).getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.layout_trinkets_item, null);

                final TrinketTable table = getItem(position);

                TextView nameView = (TextView) view.findViewById(R.id.phb_trinkets_item_name);
                nameView.setText(table.getName());

//                View quickRollBtn = view.findViewById(R.id.phb_trinkets_item_button);
//                quickRollBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        quickRoll(table);
//                    }
//                });

                return view;
            }
        };
    }

    private void quickRoll(TrinketTable table) {
        String roll = table.getRoll();
        Log.info("roll = " + roll);

        int result = evaluateRoll(roll);
        Log.info("roll evaluation = " + result);

        Log.info("Requesting readable database...");
        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

        String lg = Locale.getDefault().getISO3Language().toUpperCase();
        Log.info("Using language: " + lg);

        Log.info("Querying for this table's result for the roll.");
        Cursor cursor = db.rawQuery(SQL_GET_TABLE_ITEM, new String[] {""+table.getId(), ""+result, ""+result, lg});
        Log.info("Query returned " + cursor.getCount() + " results.");

        while (cursor.moveToNext()) {
            String resultDescription = cursor.getString(0);
            Log.info("Table result = " + resultDescription);
            Utils.of(getActivity()).showInfoDialog("Rolling " + table.getRoll() + " (" + result + ")", "" + result + ". " + resultDescription);
        }

        Log.end();
    }

    private int evaluateRoll(String roll) {
        String[] portions = roll.split("\\s*\\+\\s*");
        int result = 0;

        for (String portion : portions) {
            if (portion.matches("\\d+d\\d+")) {
                String[] r = portion.split("d");
                int dices = Integer.parseInt(r[0]);
                int diceMax = Integer.parseInt(r[1]);
                for (int i = 0; i < dices; i++) {
                    result += new Random().nextInt(diceMax) + 1;
                }
            } else {
                Log.error("The '_roll' column on table '_rollable_table' must have the pattern = '[0-9]+d[0-9]+(( )*\\+( )*<pattern>)?'");
                throw new RuntimeException("Error evaluating rollable table roll: " + roll);
            }
        }

        return result;
    }

    private class TrinketTable {
        private int id;
        private String name;
        private String description;
        private String roll;

        public TrinketTable() {
        }

        public TrinketTable(int id, String name, String description, String roll) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.roll = roll;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

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

        public void setRoll(String roll) {
            this.roll = roll;
        }

        public String getRoll() {
            return roll;
        }

        @Override
        public String toString() {
            return "TrinketTable{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", roll='" + roll + '\'' +
                    '}';
        }
    }
}
