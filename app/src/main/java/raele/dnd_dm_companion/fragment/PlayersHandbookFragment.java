package raele.dnd_dm_companion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raele.dnd_dm_companion.R;
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
            public void onClick(View v) {onBackgroundClicks();}
        });
        view.findViewById(R.id.phb_item_feats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onFeatsClicks();
            }
        });
        view.findViewById(R.id.phb_item_equipments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onEquipmentsClick();}
        });
        view.findViewById(R.id.phb_item_downtime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onDowntimeClicks();}
        });
        view.findViewById(R.id.phb_item_combat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onCombatClicks();
            }
        });
        view.findViewById(R.id.phb_item_spells).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onSpellsClicks();
            }
        });
        view.findViewById(R.id.phb_item_experience).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onExperienceClicks();}
        });
        view.findViewById(R.id.phb_item_trinkets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ontrinketsClicks();}
        });

        return view;
    }

    private void ontrinketsClicks() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onExperienceClicks() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onSpellsClicks() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onCombatClicks() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onDowntimeClicks() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onEquipmentsClick() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onFeatsClicks() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onBackgroundClicks() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onClassesClick() {
        Utils.of(getActivity()).notImplemented();
    }

    private void onRacesClick() {
        Fragment fragment = new RacesFragment();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

}
