package raele.dnd_dm_companion.fragment.phb.classes;

import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;
import raele.util.android.Utils;
import raele.util.android.log.Log;

/**
 * Created by lpr on 28/09/15.
 */
public class ClassDetailsFragment extends Fragment {

    public static final String ARGS_CLASS_ID = "CLASS_ID";

    private static final String SQL_GET_CLASS_INFO =
            "SELECT tr_name._text, _class._hit_dice, tr_armors._text, tr_weapons._text, " +
                    "tr_tools._text, tr_saves._text, tr_skills._text, tr_multicl_prer._text, " +
                    "tr_multicl_prof._text, tr_money._text, tr_equipment._text, " +
                    "tr_classoption._text " +
            "FROM _class, _translation tr_name, _translation tr_armors, _translation tr_weapons, " +
                    "_translation tr_tools, _translation tr_saves, _translation tr_skills, " +
                    "_translation tr_multicl_prer, _translation tr_multicl_prof, " +
                    "_translation tr_money, _translation tr_equipment, " +
                    "_translation tr_classoption " +
            "WHERE _class._id = ? AND " +
                    "tr_name._id = _class._name_id AND tr_name._language = ? AND " +
                    "tr_armors._id = _class._armors_id AND tr_armors._language = ? AND " +
                    "tr_weapons._id = _class._weapons_id AND tr_weapons._language = ? AND " +
                    "tr_tools._id = _class._tools_id AND tr_tools._language = ? AND " +
                    "tr_saves._id = _class._saves_id AND tr_saves._language = ? AND " +
                    "tr_skills._id = _class._skills_id AND tr_skills._language = ? AND " +
                    "tr_multicl_prer._id = _class._multicl_prer_id AND tr_multicl_prer._language = ? AND " +
                    "tr_multicl_prof._id = _class._multicl_prof_id AND tr_multicl_prof._language = ? AND " +
                    "tr_money._id = _class._money_id AND tr_money._language = ? AND " +
                    "tr_equipment._id = _class._equipment_id AND tr_equipment._language = ? AND " +
                    "tr_classoption._id = _class._classoption_name_id AND tr_classoption._language = ?";

    private static final String SQL_GET_CLASS_FEATURES =
            "SELECT tr_name._text, tr_description._text, _class_feature._level " +
            "FROM _class_feature, _feature, _translation tr_name, _translation tr_description " +
            "WHERE _class_feature._class_id = ? AND " +
                    "(_class_feature._class_option_id = ? OR _class_feature._class_option_id IS NULL) AND " +
                    "_class_feature._feature_id = _feature._id AND " +
                    "tr_name._id = _feature._name_id AND tr_name._language = ? AND " +
                    "tr_description._id = _feature._description_id AND tr_description._language = ? ";

    private static final String SQL_GET_CLASS_OPTIONS =
            "SELECT _class_option._id, tr_name._text " +
            "FROM _class_option, _translation tr_name " +
            "WHERE _class_option._class_id = ? AND " +
                    "_class_option._name_id = tr_name._id AND tr_name._language = ?";

    private static final String SQL_GET_SPEC_FEATURES =
            "SELECT tr_name._text, tr_description._text, _class_feature._level " +
            "FROM _class_feature, _feature, _translation tr_name, _translation tr_description " +
            "WHERE _class_feature._class_option_id = ? AND " +
                    "_class_feature._feature_id = _feature._id AND " +
                    "_feature._name_id = tr_name._id AND tr_name._language = ? AND" +
                    "_feature._description_id = tr_description._id AND tr_description._language = ?";

    private class ClassFeature {
        public String name;
        public String description;
        public Integer level;
    }

    private int mId;

    private String mName;
    private int mHitDice;
    private String mArmors;
    private String mWeapons;
    private String mTools;
    private String mSaves;
    private String mSkills;
    private String mMulticl_prer;
    private String mMulticl_prof;
    private String mMoney;
    private String mEquipment;
    private String mClassOptionName;

    private View mView;
    private LinearLayout mFeaturesLayout;

    private ArrayList<ClassOption> mClassOptions;
    private LinkedList<ClassFeature> mBaseFeatures = new LinkedList<>();
    private LinkedList<ClassFeature> mSpecFeatures = new LinkedList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.begin();

        if (getArguments() != null && getArguments().containsKey(ARGS_CLASS_ID)) {
            mId = getArguments().getInt(ARGS_CLASS_ID);
            Log.info("Received id " + mId);

            String lg = Locale.getDefault().getISO3Language().toUpperCase();
            Log.info("Using language " + lg);

            Log.info("Requesting readable database...");
            SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

            Log.info("Requesting phb info.");
            Cursor classInfoCursor = db.rawQuery(SQL_GET_CLASS_INFO, new String[]{"" + mId,
                    lg, lg, lg, lg, lg, lg, lg, lg, lg, lg, lg});

            Log.info("Found " + classInfoCursor.getCount() + " results.");
            if (classInfoCursor.moveToNext()) {
                mName = classInfoCursor.getString(0);
                mHitDice = classInfoCursor.getInt(1);
                mArmors = classInfoCursor.getString(2);
                mWeapons = classInfoCursor.getString(3);
                mTools = classInfoCursor.getString(4);
                mSaves = classInfoCursor.getString(5);
                mSkills = classInfoCursor.getString(6);
                mMulticl_prer = classInfoCursor.getString(7);
                mMulticl_prof = classInfoCursor.getString(8);
                mMoney = classInfoCursor.getString(9);
                mEquipment = classInfoCursor.getString(10);
                mClassOptionName = classInfoCursor.getString(11);
            } else {
                Utils.of(getActivity()).showInfoDialog("Info", "Class Not Found")
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
            }

            Cursor classOptionsCursor = db.rawQuery(SQL_GET_CLASS_OPTIONS, new String[]{"" + mId, lg});

            mClassOptions = new ArrayList<>(classOptionsCursor.getCount());
            while (classOptionsCursor.moveToNext()) {
                ClassOption classOption = new ClassOption();
                classOption.setId(classOptionsCursor.getInt(0));
                classOption.setName(classOptionsCursor.getString(1));
                mClassOptions.add(classOption);
            }

        }

        Log.end();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.begin();

        mView = inflater.inflate(R.layout.fragment_class_details, container, false);

        Log.info("Setting up view...");

        TextView titleView = (TextView) mView.findViewById(R.id.phb_class_details_name);
        TextView featuresView = (TextView) mView.findViewById(R.id.phb_class_details_features);
        TextView classTableView = (TextView) mView.findViewById(R.id.phb_class_details_table_txt);
        TextView hitDiceView = (TextView) mView.findViewById(R.id.phb_class_details_hitdice);
        TextView armorsView = (TextView) mView.findViewById(R.id.phb_class_details_armor);
        TextView weaponsView = (TextView) mView.findViewById(R.id.phb_class_details_weapons);
        TextView toolsView = (TextView) mView.findViewById(R.id.phb_class_details_tools);
        TextView savesView = (TextView) mView.findViewById(R.id.phb_class_details_saves);
        TextView skillsView = (TextView) mView.findViewById(R.id.phb_class_details_skills);
        TextView equipmentView = (TextView) mView.findViewById(R.id.phb_class_details_equipment);
        TextView wealthView = (TextView) mView.findViewById(R.id.phb_class_details_wealth);
        TextView prerequisitesView = (TextView) mView.findViewById(R.id.phb_class_details_multiclass_prerequisites);
        TextView proficienciesView = (TextView) mView.findViewById(R.id.phb_class_details_multiclass_proficiencies);
        TextView classOptionNameView = (TextView) mView.findViewById(R.id.phb_class_details_classoption_name);
        Button classTableButton = (Button) mView.findViewById(R.id.phb_class_details_table_btn);
        Spinner classOptionSpinner = (Spinner) mView.findViewById(R.id.phb_class_details_classoption_spinner);
        mFeaturesLayout = (LinearLayout) mView.findViewById(R.id.phb_class_details_level_features);

        titleView.setText(String.format(getString(R.string.phb_class_details_title), mName));
        classTableView.setText(String.format(getString(R.string.phb_class_details_table_txt), mName));
        featuresView.setText(String.format(getString(R.string.phb_class_details_features), mName));
        hitDiceView.setText("d"+mHitDice);
        armorsView.setText(mArmors);
        weaponsView.setText(mWeapons);
        toolsView.setText(mTools);
        savesView.setText(mSaves);
        skillsView.setText(mSkills);
        equipmentView.setText(mEquipment);
        wealthView.setText(mMoney);
        prerequisitesView.setText(mMulticl_prer);
        proficienciesView.setText(mMulticl_prof);
        classOptionNameView.setText(mClassOptionName);

        classTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Not implemented yet
                Utils.of(getActivity()).showShortToast("Not implemented yet.");
            }
        });

        SpinnerAdapter adapter = createClassOptionsAdapter();
        classOptionSpinner.setAdapter(adapter);
        classOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupFeatures(mClassOptions.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setupFeatures(null);

        Log.end();

        return mView;
    }

    private SpinnerAdapter createClassOptionsAdapter() {
        return new BaseAdapter() {
            @Override
            public int getCount() {
                return mClassOptions.size();
            }

            @Override
            public ClassOption getItem(int position) {
                return mClassOptions.get(position);
            }

            @Override
            public long getItemId(int position) {
                return -1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = getActivity().getLayoutInflater().inflate(android.R.layout.simple_dropdown_item_1line, null);

                TextView nameView = (TextView) view.findViewById(android.R.id.text1);
                nameView.setText(getItem(position).getName());

                return view;
            }
        };
    }

    private void setupFeatures(Integer classOptionId) {
        loadFeatures(classOptionId);

        HashMap<Integer, ViewGroup> levelViews = new HashMap<>(20);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (final ClassFeature feature : mBaseFeatures) {
            ViewGroup levelView = (ViewGroup) levelViews.get(feature.level);
            if (levelView == null) {
                levelView = (ViewGroup) inflater.inflate(R.layout.layout_phb_class_details_level, null);
                levelViews.put(feature.level, levelView);
            }

            TextView titleView = (TextView) levelView.findViewById(R.id.phb_class_details_level_title);
            String titleText = getString(R.string.phb_class_details_level_title);
            titleView.setText(String.format(titleText, ""+feature.level));

            ViewGroup featuresView = (ViewGroup) levelView.findViewById(R.id.phb_class_details_level_container);
            View featureView = inflater.inflate(R.layout.layout_phb_class_details_feature, null);
            TextView featureText = (TextView) featureView.findViewById(R.id.phb_class_details_feature_text);
            featureText.setText(feature.name);
            featureText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.of(getActivity()).showInfoDialog(feature.name, feature.description);
                }
            });
            featuresView.addView(featureView);
        }

        mFeaturesLayout.removeAllViews();
        for (int i = 1; i <= 20; i++) {
            ViewGroup levelView = levelViews.get(i);
            if (levelView != null) {
                mFeaturesLayout.addView(levelView);
            }
        }
    }

    private void loadFeatures(Integer classOptionId) {
        mBaseFeatures.clear();

        String lg = Locale.getDefault().getISO3Language().toUpperCase();

        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();

        Log.info("Querying for phb features for " + mName + "...");
        Cursor classFeaturesCursor = db.rawQuery(SQL_GET_CLASS_FEATURES, new String[]{"" + mId, "" + classOptionId, lg, lg});

        Log.info("Found " + classFeaturesCursor.getCount() + " features.");
        while (classFeaturesCursor.moveToNext()) {
            ClassFeature feature = new ClassFeature();
            feature.name = classFeaturesCursor.getString(0);
            feature.description = classFeaturesCursor.getString(1);
            feature.level = classFeaturesCursor.getInt(2);

            mBaseFeatures.add(feature);
        }

        Collections.sort(mBaseFeatures, new Comparator<ClassFeature>() {
            @Override
            public int compare(ClassFeature lhs, ClassFeature rhs) {
                return lhs.level - rhs.level;
            }
        });
    }

    private class ClassOption {
        private int id;
        private String name;

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
    }
}
