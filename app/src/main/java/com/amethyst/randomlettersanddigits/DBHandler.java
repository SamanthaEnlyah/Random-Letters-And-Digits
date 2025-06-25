package com.amethyst.randomlettersanddigits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    //Answer: sound,solution, answer, isCorrect, id

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "nasumicna_slova_i_brojevi";

    private static final String TABLE_NAME = "Answer";

    private static final String ID_COL_NAME = "Answer_ID";
    private static final String SOUND_COL_NAME = "Sound";
    private static final String SOLUTION_COL_NAME = "Solution";
    private static final String USER_SOLUTION_COL_NAME = "UserSolution";
    private static final String CORRECT_COL_NAME = "IsCorrect";



    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SOUND_COL_NAME + " TEXT,"
                + SOLUTION_COL_NAME+ " TEXT,"
                + USER_SOLUTION_COL_NAME + " TEXT,"
                + CORRECT_COL_NAME + " BOOLEAN);";
        sqLiteDatabase.execSQL(query);
    }

    public void AddAnswer(Answer answer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(SOUND_COL_NAME, answer.getSound());
        values.put(SOLUTION_COL_NAME, answer.getSolution());
        values.put(USER_SOLUTION_COL_NAME, answer.getUserSolution());
        values.put(CORRECT_COL_NAME, answer.isCorrect());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Answer> GetAnswers(){
        ArrayList<Answer> answers = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        String sql =  "SELECT * FROM " + TABLE_NAME;
        Cursor answersCursor = db.rawQuery(sql,null);
        if(answersCursor.moveToFirst()){
            do {
                answers.add(new Answer(answersCursor.getInt(0),
                        answersCursor.getString(1),
                        answersCursor.getString(2),
                        answersCursor.getString(3)
                ));

            }while (answersCursor.moveToNext());

            answersCursor.close();
        }
//        answers.add(new Answer(-1));

        return answers;
    }


    public void DeleteAnswer(int id){
        String sql = "DELETE FROM Answer WHERE " + ID_COL_NAME + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
       // db.delete(TABLE_NAME, "ID_COL_NAME=?", new String[]{Integer.toString(id)}) ;
        db.close();
    }

    public Answer GetSolutionForProblem(String problem) {
        String sql = "SELECT  Sound, Solution, Answer_ID FROM Answer WHERE " + SOUND_COL_NAME  + " = '" + problem + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Answer problemAndSolution = new Answer();

        Cursor SolutionCursor = db.rawQuery(sql, null);
        if (SolutionCursor.moveToFirst()) {
            do {
               problemAndSolution.setSound(SolutionCursor.getString(0));
               problemAndSolution.setSolution(SolutionCursor.getString(1));
               problemAndSolution.setId(SolutionCursor.getInt(2));

            } while (SolutionCursor.moveToNext());
            SolutionCursor.close();
        }
        return problemAndSolution;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
