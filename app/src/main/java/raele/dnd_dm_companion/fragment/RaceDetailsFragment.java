package raele.dnd_dm_companion.fragment;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;
import raele.util.android.Utils;
import raele.util.android.log.Log;

/**
 * Created by lpr on 22/09/15.
 */
public class RaceDetailsFragment extends Fragment {

    public static final String ARG_RACE_ID = "RACE_ID";

    private static final String GET_RACE_DETAILS_SQL =
            "SELECT tr_name._text, _sub_race._height, _sub_race._weight, tr_size._text, " +
                    "tr_adult._text, tr_max._text, tr_alignment._text, tr_ability._text, " +
                    "_sub_race._speed, tr_languages._text, _sub_race._darkvision, " +
                    "_sub_race._super_race_id " +
            "FROM _sub_race, _size, _translation tr_name, _translation tr_size, " +
                    "_translation tr_adult, _translation tr_max, _translation tr_alignment, " +
                    "_translation tr_ability, _translation tr_languages " +
            "WHERE _sub_race._id = ? AND " +
                    "tr_name._id = _sub_race._name_id AND tr_name._language = ? AND " +
                    "_size._id = _sub_race._size_id AND tr_size._id = _size._description_id AND tr_size._language = ? AND " +
                    "tr_adult._id = _sub_race._adult_age_id AND tr_adult._language = ? AND " +
                    "tr_max._id = _sub_race._max_age_id AND tr_max._language = ? AND " +
                    "tr_alignment._id = _sub_race._alignment_id AND tr_alignment._language = ? AND " +
                    "tr_ability._id = _sub_race._ability_id AND tr_ability._language = ? AND " +
                    "tr_languages._id = _sub_race._languages_id AND tr_languages._language = ?";

    private static final String GET_RACIAL_TRAITS_SQL =
            "SELECT tr_name._text, tr_desc._text " +
            "FROM _feature, _racial_trait, _translation tr_name, _translation tr_desc " +
            "WHERE _racial_trait._sub_race_id = ? AND " +
                    "_feature._id = _racial_trait._feature_id AND " +
                    "tr_name._id = _feature._name_id AND tr_name._language = ? AND " +
                    "tr_desc._id = _feature._description_id AND tr_desc._language = ?";

    private static final String GET_NAME_SAMPLES_SQL =
            "SELECT _sample_name._name, tr_type._text " +
            "FROM _sample_name, _name_type, _translation tr_type " +
            "WHERE _sample_name._race_id = ? AND " +
                    "_name_type._id = _sample_name._type_id AND " +
                    "tr_type._id = _name_type._description_id AND tr_type._language = ?";

    private static final class TraitInfo {
        public String name;
        public String description;

        public TraitInfo(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    private String mName;
    private String mHeight;
    private String mWeight;
    private String mSize;
    private String mAdultAge;
    private String mMaxAge;
    private String mAlignment;
    private String mAbility;
    private int mSpeed;
    private String mLanguages;
    private int mDarkvision;
    private int mSuperRaceId;

    private ArrayList<TraitInfo> mTraits;
    private HashMap<String, LinkedList<String>> mSampleNames; // Type -> List<Sample Names>

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.begin();

        Log.info("Reading received arguments...");
        Bundle args = getArguments();
        if (args == null || !args.containsKey(ARG_RACE_ID)) {
            Log.error("No arguments found.");
        } else {
            int id = args.getInt(ARG_RACE_ID);
            Log.info("Received id: " + id);

            String lang = Locale.getDefault().getISO3Language().toUpperCase();
            Log.info("Querying for entries for language: " + lang);

            Log.info("Requesting database read access...");
            SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

            Log.info("Querying race data...");
            Cursor raceDataCursor = db.rawQuery(GET_RACE_DETAILS_SQL,
                    new String[]{""+id, lang, lang, lang, lang, lang, lang, lang});

            Log.info("Found " + raceDataCursor.getCount() + " results.");
            if (raceDataCursor.moveToNext()) {
                mName = raceDataCursor.getString(0);
                mHeight = raceDataCursor.getString(1);
                mWeight = raceDataCursor.getString(2);
                mSize = raceDataCursor.getString(3);
                mAdultAge = raceDataCursor.getString(4);
                mMaxAge = raceDataCursor.getString(5);
                mAlignment = raceDataCursor.getString(6);
                mAbility = raceDataCursor.getString(7);
                mSpeed = raceDataCursor.getInt(8);
                mLanguages = raceDataCursor.getString(9);
                mDarkvision = raceDataCursor.getInt(10);
                mSuperRaceId = raceDataCursor.getInt(11);
            }

            Log.info("Querying racial traits data...");
            Cursor traitsDataCursor = db.rawQuery(GET_RACIAL_TRAITS_SQL, new String[] {""+id, lang, lang});

            Log.info("Found " + traitsDataCursor.getCount() + " results.");
            mTraits = new ArrayList<>(traitsDataCursor.getCount());
            while (traitsDataCursor.moveToNext()) {
                String traitName = traitsDataCursor.getString(0);
                String traitDescription = traitsDataCursor.getString(1);
                mTraits.add(new TraitInfo(traitName, traitDescription));
            }

            Log.info("Querying sample names...");
            Cursor sampleNamesCursor = db.rawQuery(GET_NAME_SAMPLES_SQL, new String[]{""+mSuperRaceId, lang});

            Log.info("Found " + sampleNamesCursor.getCount() + " results.");
            mSampleNames = new HashMap<>(3);
            while (sampleNamesCursor.moveToNext()) {
                String name = sampleNamesCursor.getString(0);
                String type = sampleNamesCursor.getString(1);

                LinkedList<String> names = mSampleNames.get(type);
                if (names == null) {
                    names = new LinkedList<String>();
                    mSampleNames.put(type, names);
                }

                names.add(name);
            }

            Log.info("All data loaded successfully.");
        }

        Log.end();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.begin();

        Log.info("Inflating the view...");
        View view = inflater.inflate(R.layout.fragment_race_details, container, false);

        Log.info("Setting the view up...");
        TextView.class.cast(view.findViewById(R.id.phb_races_details_name)).setText(mName);
        TextView.class.cast(view.findViewById(R.id.phb_races_details_height)).setText(mHeight);
        TextView.class.cast(view.findViewById(R.id.phb_races_details_weight)).setText(mWeight);
        TextView.class.cast(view.findViewById(R.id.phb_races_details_size)).setText(mSize);
        TextView.class.cast(view.findViewById(R.id.phb_races_details_adult_age)).setText(mAdultAge);
        TextView.class.cast(view.findViewById(R.id.phb_races_details_max_age)).setText(mMaxAge);
        TextView.class.cast(view.findViewById(R.id.phb_races_details_alignment)).setText(mAlignment);
        TextView.class.cast(view.findViewById(R.id.phb_races_details_ability_score)).setText(mAbility);
        TextView.class.cast(view.findViewById(R.id.phb_races_details_speed)).setText("" + mSpeed + " ft.");
        TextView.class.cast(view.findViewById(R.id.phb_races_details_languages)).setText(mLanguages);
        String darkvision;
        if (mDarkvision == 0) {
            darkvision = getString(R.string.phb_races_details_darkvision_no);
        } else {
            darkvision = String.format(getString(R.string.phb_races_details_darkvision_yes), "" + mDarkvision);
        }
        TextView darkvisionView = TextView.class.cast(view.findViewById(R.id.phb_races_details_darkvision));
        darkvisionView.setText(darkvision);
        darkvisionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDarkvisionClick();
            }
        });

        Log.info("Inflating trait views...");
        ViewGroup traits = (ViewGroup) view.findViewById(R.id.phb_races_details_other);
        for (final TraitInfo trait : mTraits) {
            View traitView = inflater.inflate(R.layout.layout_phb_races_details_other_item, null);
            TextView.class.cast(traitView.findViewById(R.id.phb_races_details_other_item)).setText(trait.name);
            traitView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTraitClick(trait);
                }
            });
            traits.addView(traitView);
        }

        Log.info("Inflating sample names' views...");
        ViewGroup names = (ViewGroup) view.findViewById(R.id.phb_races_details_names_container);
        for (Map.Entry<String, LinkedList<String>> entry : mSampleNames.entrySet()) {
            String type = entry.getKey();
            LinkedList<String> sampleNames = entry.getValue();

            StringBuilder builder = new StringBuilder();
            for (String name : sampleNames) {
                builder.append(name);
                builder.append(", ");
            }

            ViewGroup block = (ViewGroup) inflater.inflate(R.layout.layout_phb_races_details_names_block, null);
            TextView.class.cast(block.findViewById(R.id.phb_races_details_names_type)).setText(type);
            TextView.class.cast(block.findViewById(R.id.phb_races_details_names_samples)).setText(builder.toString());

            names.addView(block);
        }

        Log.info("Everything ok.");
        Log.end();
        return view;
    }

    private void onDarkvisionClick() {
        String title = getString(R.string.phb_races_details_darkvision);
        String description = String.format(
                getString(R.string.phb_races_details_darkvision_description),
                "" + mDarkvision
        );
        Utils.of(getActivity()).showInfoDialog(title, description);
    }

    private void onTraitClick(TraitInfo trait) {
        Utils.of(getActivity()).showInfoDialog(trait.name, trait.description);
    }
}
