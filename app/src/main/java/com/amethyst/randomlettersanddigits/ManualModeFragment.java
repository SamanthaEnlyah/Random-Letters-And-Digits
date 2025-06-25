package com.amethyst.randomlettersanddigits;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManualModeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualModeFragment extends Fragment {


    private EditText editTextProblem;

    public ManualModeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_manual_mode, container, false);
        editTextProblem = view.findViewById(R.id.editTextProblem);
        // Inflate the layout for this fragment
        return view;
    }

    public EditText getEditTextProblem() {
        return editTextProblem;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //notify main activity that fragment is created
        ((MainActivity)getActivity()).onManualModeReady(editTextProblem);
    }
}