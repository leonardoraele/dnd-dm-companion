package raele.dnd_dm_companion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import raele.dnd_dm_companion.R;
import raele.dnd_dm_companion.database.SubRace;
import raele.util.Collections;

/**
 * Created by lpr on 21/09/15.
 */
public class RacesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_races, container, false);

        ListView listView = (ListView) view.findViewById(R.id.phb_races_menu_list);
        ListAdapter adapter = createRacesListAdapter();
        listView.setAdapter(adapter);

        /*
        view.findViewById(R.id.phb_races_human_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        view.findViewById(R.id.phb_races_human_variant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        view.findViewById(R.id.phb_races_dwarf_hill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        view.findViewById(R.id.phb_races_dwarf_mountain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        view.findViewById(R.id.phb_races_elf_high).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        view.findViewById(R.id.phb_races_elf_wood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        view.findViewById(R.id.phb_races_elf_drow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        view.findViewById(R.id.phb_races_halfling_lightfoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        view.findViewById(R.id.phb_races_halfling_stout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.of(getActivity()).notImplemented();}
        });
        */

        return view;
    }

    private ListAdapter createRacesListAdapter() {
        List<Model> result = new Select().from(SubRace.class).execute();
        List<Integer> nameIds = Collections.map(result, new ArrayList<Integer>(result.size()),
                new Collections.Mapper<Model, Integer>() {
            @Override
            public Integer map(Model model) {
                return SubRace.class.cast(model).nameTextId;
            }
        });
        List raceNames =
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.layout_races_menu_item, R.id.phb_races_menu_item_name, raceNames);
    }

}
