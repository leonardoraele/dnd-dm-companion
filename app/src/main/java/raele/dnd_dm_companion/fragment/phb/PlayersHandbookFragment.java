package raele.dnd_dm_companion.fragment.phb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.fragment.phb.classes.ClassListFragment;
import raele.dnd_dm_companion.fragment.phb.feat.FeatListFragment;
import raele.dnd_dm_companion.fragment.phb.races.RaceListFragment;
import raele.dnd_dm_companion.fragment.phb.trinkets.TrinketListFragment;

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
        view.findViewById(R.id.phb_item_feats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onFeatsClick();
            }
        });
        view.findViewById(R.id.phb_item_trinkets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onTrinketsClick();}
        });

        return view;
    }

    private void onTrinketsClick() {
        TrinketListFragment fragment = new TrinketListFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

    private void onFeatsClick() {
        Fragment fragment = new FeatListFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

    private void onClassesClick() {
        Fragment fragment = new ClassListFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

    private void onRacesClick() {
        Fragment fragment = new RaceListFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(toString())
                .replace(R.id.container, fragment)
                .commit();
    }

}
