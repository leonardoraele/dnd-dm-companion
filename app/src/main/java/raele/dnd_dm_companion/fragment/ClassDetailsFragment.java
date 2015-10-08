package raele.dnd_dm_companion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raele.dnd_dm_companion.R;

/**
 * Created by lpr on 28/09/15.
 */
public class ClassDetailsFragment extends Fragment {

    public static final String ARGS_CLASS_ID = "CLASS_ID";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARGS_CLASS_ID)) {
            int id = getArguments().getInt(ARGS_CLASS_ID);

            // TODO Get data from database
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_details, container, false);

        // TODO Set data to view

        return view;
    }

}
