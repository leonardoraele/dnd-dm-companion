package raele.dnd_dm_companion.fragment.phb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.fragment.phb.classes.ClassesFragment;
import raele.dnd_dm_companion.fragment.phb.races.RacesFragment;
import raele.dnd_dm_companion.fragment.phb.trinkets.TrinketsFragment;
import raele.util.android.Utils;

/**
 * Created by lpr on 21/09/15.
 */
public class PlayersHandbookFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players_handbook, container, false);

        // Configure button clicks
        view.findViewById(R.id.phb_item_races).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onRacesClick();    }
        });
        view.findViewById(R.id.phb_item_classes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onClassesClick();
            }
        });
        view.findViewById(R.id.phb_item_backgrounds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackgroundsClicks();}
        });
        view.findViewById(R.id.phb_item_feats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeatsClick();
            }
        });
        view.findViewById(R.id.phb_item_equipments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onEquipmentsClick();}
        });
        view.findViewById(R.id.phb_item_downtime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDowntimeClick();}
        });
        view.findViewById(R.id.phb_item_combat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCombatClick();
            }
        });
        view.findViewById(R.id.phb_item_spells).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpellsClick();
            }
        });
        view.findViewById(R.id.phb_item_experience).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExperienceClick();}
        });
        view.findViewById(R.id.phb_item_trinkets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrinketsClick();}
        });

        return view;
    }

    private void onTrinketsClick() {
        TrinketsFragment fragment = new TrinketsFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

    private void onExperienceClick() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onSpellsClick() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onCombatClick() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onDowntimeClick() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onEquipmentsClick() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onFeatsClick() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onBackgroundsClicks() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onClassesClick() {
        Fragment fragment = new ClassesFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

    private void onRacesClick() {
        Fragment fragment = new RacesFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

}
