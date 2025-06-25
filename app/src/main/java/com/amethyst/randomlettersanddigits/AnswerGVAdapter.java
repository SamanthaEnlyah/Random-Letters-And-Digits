package com.amethyst.randomlettersanddigits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class AnswerGVAdapter extends ArrayAdapter<Answer> {

    public AnswerGVAdapter(@NonNull Context context, ArrayList<Answer> answerArrayList) {
        super(context, 0, answerArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.gv_answer, parent, false);
        }

        Answer answerModel = getItem(position);

        TextView id = listitemView.findViewById(R.id.textViewID);
        TextView sound = listitemView.findViewById(R.id.textViewUserSoundPlaceholder);
        TextView solution = listitemView.findViewById(R.id.textViewSolutionPlaceholder);
        TextView userSolution = listitemView.findViewById(R.id.textViewUserSolutionPlaceholder);
        TextView isSolutionCorrect = listitemView.findViewById(R.id.textViewIsCorrectPlaceholder);


//        if(answerModel.getId() == -1) {
//            //id.setText("ID");
//            sound.setText("Problem");
//            userSolution.setText("Your solution");
//            solution.setText("Solution");
//            isSolutionCorrect.setText("Is it Correct");
//        } else {
            id.setText(Integer.toString(answerModel.getId()));
            sound.setText(answerModel.getSound());
        userSolution.setText(answerModel.getUserSolution());
            solution.setText(answerModel.getSolution());
            isSolutionCorrect.setText(answerModel.checkAnswer());
//        }
            return listitemView;
    }
}

