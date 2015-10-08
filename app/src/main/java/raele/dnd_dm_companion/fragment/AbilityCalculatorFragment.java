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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.DbHelper;

/**
 * Created by lpr on 08/10/15.
 */
public class AbilityCalculatorFragment extends Fragment {

    private static HashMap<Integer, Integer> sCostMap = createCostMap();
    private static Integer sBaseAbility = 8;
    private static Integer sMaxAbility = 15;

    private static HashMap<Integer, Integer> createCostMap() {
        HashMap<Integer, Integer> map = new HashMap<>(6);
        map.put(8, 0);
        map.put(9, 1);
        map.put(10, 2);
        map.put(11, 3);
        map.put(12, 4);
        map.put(13, 5);
        map.put(14, 7);
        map.put(15, 9);
        return map;
    }

    private static final String SQL_GET_ALL_SUB_RACES =
            "SELECT tr_name._text, tr_ability._text " +
            "FROM _sub_race, _translation tr_name, _translation tr_ability " +
            "WHERE tr_name._id = _sub_race._name_id AND tr_name._language = ? AND " +
                  "tr_ability._id = _sub_race._ability_id AND tr_ability._language = ?";

    private class Race {
        public String name;
        public String ability;

        public Race(String name, String ability) {
            this.name = name;
            this.ability = ability;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private class Ability {
        public int base;
        public int bonus;
        public EditText baseValueEditText;
        public EditText bonusValueEditText;
        public TextView totalValueTextView;
        public TextView modifierTextView;
        public TextView pointsTextView;
    }

    private ArrayList<Race> mRaces;
    private LinkedList<Ability> mAbilities;
    private TextView mRemainingPointsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String language = Locale.getDefault().getISO3Language().toUpperCase();

        SQLiteDatabase db = new DbHelper(getActivity()).getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_ALL_SUB_RACES, new String[] {language, language});

        mRaces = new ArrayList<>(cursor.getCount() + 1);
        mRaces.add(new Race("--", ""));
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String ability = cursor.getString(1);
            mRaces.add(new Race(name, ability));
        }

        mAbilities = new LinkedList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ability_calculator, container, false);

        final TextView raceDescription = (TextView) view.findViewById(R.id.calc_description);

        Spinner raceSpinner = (Spinner) view.findViewById(R.id.culc_race_spinner);
        SpinnerAdapter raceSpinnerAdapter = new ArrayAdapter<Race>(getActivity(), android.R.layout.simple_dropdown_item_1line, mRaces);
        raceSpinner.setAdapter(raceSpinnerAdapter);
        raceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ability = mRaces.get(position).ability;
                raceDescription.setText(ability);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        view.findViewById(R.id.calc_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        mRemainingPointsView = (TextView) view.findViewById(R.id.calc_remaining_points);

        ViewGroup layout = (ViewGroup) view.findViewById(R.id.calc_abilities_box);
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_str));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_dex));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_con));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_int));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_wis));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_cha));

        return view;
    }

    private void reset() {
        for (Ability ability : mAbilities) {
            ability.base = sBaseAbility;
            ability.bonus = 0;
            refresh(ability);
        }
    }

    private View createAbilityBox(LayoutInflater inflater, int abilityNameResource) {
        final Ability ability = new Ability();
        ability.base = sBaseAbility;
        ability.bonus = 0;

        View view = inflater.inflate(R.layout.layout_calc_ability, null);

        TextView abilityNameView = (TextView) view.findViewById(R.id.calc_ability_name);
        abilityNameView.setText(getString(abilityNameResource));

        View baseMinusButton = view.findViewById(R.id.calc_base_minus);
        View basePlusButton = view.findViewById(R.id.calc_base_plus);
        View bonusMinusButton = view.findViewById(R.id.calc_bonus_minus);
        View bonusPlusButton = view.findViewById(R.id.calc_bonus_plus);

        baseMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseBase(ability);
            }
        });

        basePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseBase(ability);
            }
        });

        bonusMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseBonus(ability);
            }
        });

        bonusPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseBonus(ability);
            }
        });
        
        ability.baseValueEditText = (EditText) view.findViewById(R.id.calc_base_value);
        ability.bonusValueEditText = (EditText) view.findViewById(R.id.calc_bonus_value);
        ability.totalValueTextView = (TextView) view.findViewById(R.id.calc_total);
        ability.modifierTextView = (TextView) view.findViewById(R.id.calc_modifier);
        ability.pointsTextView = (TextView) view.findViewById(R.id.calc_point_spent);

        ability.baseValueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int newValue = Integer.valueOf(ability.baseValueEditText.getText().toString()).intValue();
                ability.base = newValue;
                refresh(ability);
            }
        });

        ability.bonusValueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int newValue = Integer.valueOf(ability.bonusValueEditText.getText().toString()).intValue();
                ability.bonus = newValue;
                refresh(ability);
            }
        });

        refresh(ability);
        mAbilities.add(ability);

        return view;
    }

    private void increaseBonus(Ability ability) {
        ability.bonus++;
        refresh(ability);
    }

    private void decreaseBonus(Ability ability) {
        ability.bonus--;
        refresh(ability);
    }

    private void increaseBase(Ability ability) {
        if (ability.base < sMaxAbility) {
            ability.base++;
            refresh(ability);
        }
    }

    private void decreaseBase(Ability ability) {
        if (ability.base > sBaseAbility) {
            ability.base--;
            refresh(ability);
        }
    }

    private void refresh(Ability ability) {
        ability.baseValueEditText.setText("" + ability.base);
        ability.bonusValueEditText.setText("" + ability.bonus);

        int total = ability.base + ability.bonus;
        ability.totalValueTextView.setText("" + total);
        ability.modifierTextView.setText("" + getModifier(total));
        ability.pointsTextView.setText("" + sCostMap.get(ability.base));

        refreshTotalPoints();
    }

    private void refreshTotalPoints() {
        int pointsSpent = 0;
        for (Ability ability : mAbilities) {
            pointsSpent += sCostMap.get(ability.base);
        }

        int remaining = 27 - pointsSpent;
        mRemainingPointsView.setText("" + remaining);
    }

    private int getModifier(int score) {
        return (int) Math.floor(Double.valueOf(score - 10) / 2);
    }

}
