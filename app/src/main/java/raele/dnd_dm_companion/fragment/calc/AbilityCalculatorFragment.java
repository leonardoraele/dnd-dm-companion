package raele.dnd_dm_companion.fragment.calc;

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
import raele.util.android.Utils;
import raele.util.android.log.Log;

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

    private LinkedList<Ability> mAbilities;
    private TextView mRemainingPointsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAbilities = new LinkedList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.begin();

        Log.info("Inflating the view...");
        View view = inflater.inflate(R.layout.fragment_ability_calculator, container, false);

        Log.info("Setting up setting buttons...");
        view.findViewById(R.id.calc_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        view.findViewById(R.id.calc_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {config();
            }
        });

        mRemainingPointsView = (TextView) view.findViewById(R.id.calc_remaining_points);

        Log.info("Setting up abilities views...");
        ViewGroup layout = (ViewGroup) view.findViewById(R.id.calc_abilities_box);
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_str));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_dex));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_con));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_int));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_wis));
        layout.addView(createAbilityBox(inflater, R.string.calc_ability_cha));

        Log.end();

        return view;
    }

    private void config() {
        Utils.of(getActivity()).showShortToast("Not implemented yet.");
    }

    private void reset() {
        Log.begin();
        for (Ability ability : mAbilities) {
            ability.base = sBaseAbility;
            ability.bonus = 0;
            refresh(ability);
        }
        Log.end();
    }

    private View createAbilityBox(LayoutInflater inflater, int abilityNameResource) {
        Log.begin();
        Log.info("Creating the ability...");

        final Ability ability = new Ability();
        ability.base = sBaseAbility;
        ability.bonus = 0;

        Log.info("Ability created: " + ability.toString());

        Log.info("Setting up ability's views...");

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

        Log.info("Saving ability's views...");
        ability.baseValueEditText = (EditText) view.findViewById(R.id.calc_base_value);
        ability.bonusValueEditText = (EditText) view.findViewById(R.id.calc_bonus_value);
        ability.totalValueTextView = (TextView) view.findViewById(R.id.calc_total);
        ability.modifierTextView = (TextView) view.findViewById(R.id.calc_modifier);
        ability.pointsTextView = (TextView) view.findViewById(R.id.calc_point_spent);

        ability.baseValueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.begin();
                Log.info("Exiting focus from ability's base value edit text.");
                int newValue = Integer.valueOf(ability.baseValueEditText.getText().toString()).intValue();
                Log.info("Data changed from " + ability.bonus + " to " + newValue);
                ability.base = newValue;
                Log.info("Refreshing ability views...");
                refresh(ability);
                Log.end();
            }
        });

        ability.bonusValueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.begin();
                Log.info("Exiting focus from ability's bonus edit text.");
                int newValue = Integer.valueOf(ability.bonusValueEditText.getText().toString()).intValue();
                Log.info("Data changed from " + ability.bonus + " to " + newValue);
                ability.bonus = newValue;
                Log.info("Refreshing ability views...");
                refresh(ability);
                Log.end();
            }
        });

        Log.info("Refreshing ability for the first time...");
        refresh(ability);

        Log.info("Adding the created ability to the list...");
        mAbilities.add(ability);

        Log.end();

        return view;
    }

    private void increaseBonus(Ability ability) {
        Log.begin();
        Log.info("Increasing ability's bonus: " + ability);
        ability.bonus++;
        refresh(ability);
        Log.end();
    }

    private void decreaseBonus(Ability ability) {
        Log.begin();
        Log.info("Decreasing ability's bonus: " + ability);
        ability.bonus--;
        refresh(ability);
        Log.end();
    }

    private void increaseBase(Ability ability) {
        Log.begin();
        if (ability.base < sMaxAbility) {
            Log.info("Increasing ability's base value: " + ability);
            ability.base++;
            refresh(ability);
        }
        Log.end();
    }

    private void decreaseBase(Ability ability) {
        Log.begin();
        if (ability.base > sBaseAbility) {
            Log.info("Decreasing ability's base value: " + ability);
            ability.base--;
            refresh(ability);
        }
        Log.end();
    }

    private void refresh(Ability ability) {
        Log.begin();
        ability.baseValueEditText.setText("" + ability.base);
        ability.bonusValueEditText.setText("" + ability.bonus);

        int total = ability.base + ability.bonus;
        ability.totalValueTextView.setText("" + total);
        int modifier = getModifier(total);
        ability.modifierTextView.setText(modifier > 0 ? "+" + modifier : "" + modifier);
        ability.pointsTextView.setText("" + sCostMap.get(ability.base));

        refreshTotalPoints();
        Log.end();
    }

    private void refreshTotalPoints() {
        Log.begin();
        int pointsSpent = 0;
        for (Ability ability : mAbilities) {
            pointsSpent += sCostMap.get(ability.base);
        }

        int remaining = 27 - pointsSpent;
        Log.info("Remaining points: " + remaining);
        mRemainingPointsView.setText("" + remaining);
        Log.end();
    }

    private int getModifier(int score) {
        return (int) Math.floor(Double.valueOf(score - 10) / 2);
    }

}
