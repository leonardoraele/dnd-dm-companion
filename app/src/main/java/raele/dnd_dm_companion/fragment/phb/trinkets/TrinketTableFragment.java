package raele.dnd_dm_companion.fragment.phb.trinkets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;
import raele.util.android.Utils;
import raele.util.android.log.Log;

/**
 * Created by lpr on 20/11/15.
 */
public class TrinketTableFragment extends Fragment {

    public static final String ARG_TABLE_ID = "ARG_TABLE_ID";

    private static final String GET_ROLLABLE_TABLE_INFO =
            "SELECT tr_name._text, tr_description._text, _rollable_table._roll, tr_source._text " +
            "FROM _rollable_table, _translation tr_name, _translation tr_description, _source, _translation tr_source " +
            "WHERE _rollable_table._id = ? AND " +
                    "_rollable_table._name_id = tr_name._id AND tr_name._language = ? AND " +
                    "_rollable_table._description_id = tr_description._id AND tr_description._language = ? AND " +
                    "_rollable_table._source_id = _source._id AND " +
                    "_source._description_id = tr_source._id AND tr_source._language = ?";

    private static final String GET_ALL_TABLE_ITEMS =
            "SELECT tr_description._text, _table_item._range_from, _table_item._range_to " +
            "FROM _table_item, _translation tr_description " +
            "WHERE _table_item._rollable_table_id = ? AND " +
                    "_table_item._description_id = tr_description._id AND tr_description._language = ?";

    private String mRoll;
    private String mTableName;
    private String mTableDescription;
    private String mSourceName;

    private Trinket[] mTrinkets;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int tableId = getArguments().getInt(ARG_TABLE_ID);

        String lg = Locale.getDefault().getISO3Language().toUpperCase();

        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

        Cursor tableInfoCursor = db.rawQuery(GET_ROLLABLE_TABLE_INFO, new String[]{"" + tableId, lg, lg, lg});

        if (tableInfoCursor.moveToNext()) {
            mTableName = tableInfoCursor.getString(0);
            mTableDescription = tableInfoCursor.getString(1);
            mRoll = tableInfoCursor.getString(2);
            mSourceName = tableInfoCursor.getString(3);
        } else {
            throw new RuntimeException("Unkown rollable table id.");
        }

        tableInfoCursor.close();

        Cursor itemsCursor = db.rawQuery(GET_ALL_TABLE_ITEMS, new String[]{"" + tableId, lg});

        mTrinkets = new Trinket[itemsCursor.getCount()];
        for (int i = 0; itemsCursor.moveToNext(); i++) {
            Trinket trinket = new Trinket();
            trinket.setName(itemsCursor.getString(0));
            trinket.setRangeFrom(itemsCursor.getInt(1));
            trinket.setRangeTo(itemsCursor.getInt(2));
            mTrinkets[i] = trinket;
        }

        itemsCursor.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trinket_table, container, false);

        TextView tableNameView = (TextView) view.findViewById(R.id.trinket_table_name);
        tableNameView.setText(mTableName);

        TextView tableDescriptionView = (TextView) view.findViewById(R.id.trinket_table_description);
        tableDescriptionView.setText(mTableDescription);

        TextView tableRollView = (TextView) view.findViewById(R.id.trinket_table_roll_desc);
        tableRollView.setText(mRoll);

        View rollButton = view.findViewById(R.id.trinket_table_roll_btn);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRollClick();
            }
        });

        ListView itemsList = (ListView) view.findViewById(R.id.trinket_table_list);
        ListAdapter adapter = createItemsAdapter();
        itemsList.setAdapter(adapter);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.of(getActivity()).showInfoDialog("Trinket", mTrinkets[position].getName());
            }
        });

        return view;
    }

    private ListAdapter createItemsAdapter() {
        return new BaseAdapter() {
            @Override
            public int getCount() {
                return mTrinkets.length;
            }

            @Override
            public Trinket getItem(int position) {
                return mTrinkets[position];
            }

            @Override
            public long getItemId(int position) {
                return -1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);

                Trinket trinket = getItem(position);

                TextView name = (TextView) view.findViewById(android.R.id.text1);
                name.setText(
                        "" + trinket.getRangeFrom() +
                                (
                                        trinket.getRangeFrom() != trinket.getRangeTo() ?
                                                "-" + trinket.getRangeTo() : ""
                                )
                        + ". " + trinket.getName()
                );

                return view;
            }
        };
    }

    private void onRollClick() {
        Log.begin();

        Log.info("Rolling " + mRoll + "...");
        int result = evaluateRoll(mRoll);

        Log.info("Result: " + result);
        String resultDescription = mTrinkets[result - 1].getName();
        Log.info("Trinket: " + resultDescription);

        Utils.of(getActivity()).showInfoDialog("Rolling " + mRoll + " (" + result + ")", "" + result + ". " + resultDescription);

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

    private class Trinket {
        private String name;
        private int rangeFrom;
        private int rangeTo;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setRangeFrom(int rangeFrom) {
            this.rangeFrom = rangeFrom;
        }

        public int getRangeFrom() {
            return rangeFrom;
        }

        public void setRangeTo(int rangeTo) {
            this.rangeTo = rangeTo;
        }

        public int getRangeTo() {
            return rangeTo;
        }
    }
}
