package com.amethyst.randomlettersanddigits;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class AutoModeFragment extends Fragment {

    private TextView textViewProblemText;
    private EditText editTextNumberSoundLength;



    public AutoModeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("oncreate","fragment:" + this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("oncreateview","fragment:" + this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auto_mode, container, false);
        textViewProblemText = view.findViewById(R.id.textViewPlayedSoundInText);

        editTextNumberSoundLength = view.findViewById(R.id.editTextNumberSoundLength);
        editTextNumberSoundLength.setText(new Integer(3).toString());


        return view;
    }

    public TextView getTextViewPlayedSoundInText(){
        return textViewProblemText;
    }

    public EditText getEditTextNumberSoundLength() {
        return editTextNumberSoundLength;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LinearLayout ll = view.findViewById(R.id.root_layout);
        CardView cv = (CardView)view.findViewById(R.id.cardView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        ll.post(() -> {
            int width = ll.getWidth();
            Log.d("Viewport", "Root view width: " + width);
            int left_margin = 90;
            params.setMargins(left_margin,30,left_margin,30);
            cv.setLayoutParams(params);
        });

//        params.gravity = Gravity.CENTER;


        //inform main activity that fragment is created
        ((MainActivity)getActivity()).onAutoModeReady(textViewProblemText, editTextNumberSoundLength);
    }
}