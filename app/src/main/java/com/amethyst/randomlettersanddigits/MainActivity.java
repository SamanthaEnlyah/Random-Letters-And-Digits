package com.amethyst.randomlettersanddigits;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import kotlin.Triple;

public class MainActivity extends AppCompatActivity {

    private class PlaySound extends Thread{

        public void run(){
            if(playSounds){
                for (MediaPlayer sound: mp) {
                    try {
                        sound.start();
                        Thread.sleep(sound.getDuration());
                        Log.d("sound duration",sound.getDuration() +"");
                        sound.release();
                    }catch(InterruptedException inex){}

                }

                //playSounds = false;
            }
            mp = null;
            mp = new LinkedList<>();
            for (Triple t : chosenSounds) {
                mp.add(MediaPlayer.create(MainActivity.this, (Integer)t.getThird()));
            }
            playSounds = false;
        }
    }

    TextView viewPlayedSound;
    EditText editTextNumberSoundLength;

    Random r;
    GridView answersGV;
    LinkedList<MediaPlayer> mp;

    LinkedList<Triple> chosenSounds;

    String sound;

    LinkedList<Triple> lettersResources;
    LinkedList<Triple> digitsResources;

    LinkedList<Triple> solution;

    LinkedList<Triple> sortedChosenDigits;
    LinkedList<Triple> sortedChosenLetters;


    LinkedList<Triple> sortedChosenDigitsMyProblem;
    LinkedList<Triple> sortedChosenLettersMyProblem;

    // LinkedList<Triple> chosenDigits;

    LinkedList<Pair<Integer, String>> latinAlphabet;
    Map<String, String> latinToCyrillicForConversion;

    EditText userSolution;

    LinkedList<Triple> solutionToMyProblem;
    ArrayList<Answer> answers;

    DBHandler db;
    int selectedAnswerID;

    boolean playSounds;

    boolean alreadyTried;
    String problemAlreadyTried;

    EditText problemET;

    TextView tcheck;
    TextView tsolution;

//    LinkedList<Integer> letters;
//    LinkedList<Integer> digits;


    //SCROLL
    private int listViewTouchAction;
    private static final int MAXIMUM_LIST_ITEMS_VIEWABLE = 99;

    TabLayout tabLayout;
    ViewPager2 viewPager;

    ScaleGestureDetector scaleDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        //CREATING TABS AND ATTACHING THEM TO MAIN ACTIVITY
        tabLayout = findViewById(R.id.tabLayout);
         viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        Log.d("adapter item count: ", adapter.getItemCount() + "");

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            Log.d("Mediator", "");
            if (position == 0)  {
                tab.setText("Auto Mode");
                Log.d("Auto mode name", tab.getText().toString());
            }
            else {
                tab.setText("Manual Mode");

                Log.d("naming Manual mode", tab.getText().toString());
            }
        }).attach();

        //This is so that tab width is same as width of the screen, not wider.
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //setting width of scrollview to smallestwidth on android phone in developers options
//        Configuration config = getResources().getConfiguration();
//        int smallestWidthDp = config.smallestScreenWidthDp;

//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        float widthInPx = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                (float) smallestWidthDp,
//                displayMetrics
//        );

//        ScrollView scrollView = findViewById(R.id.scrollvertical);
//        ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
//        layoutParams.width = (int) smallestWidthDp;
//        scrollView.setLayoutParams(layoutParams);
//        scrollView.requestLayout();


//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//            tab.setText(position == 0 ? "Auto Mode" : "Manual Mode");
//            Log.d("Mediator", "");
//        }).attach();
        //END OF ATTACHING

        //CREATING FRAGMENTS IN ONCREATE IS NOT POSSIBLE:
        //Start
//        viewPager.setCurrentItem(0, false);
//        viewPager.setCurrentItem(1, false);


//        adapter.createFragment(0);
//        adapter.createFragment(1);
//        viewPager.setOffscreenPageLimit(2);
//
//        AutoModeFragment autoFragment = (AutoModeFragment) getSupportFragmentManager()
//                .findFragmentByTag("f" + viewPager.getId() + ":0");
//
//        Log.d("viewpager.getid", viewPager.getId()+"");
//        //AutoModeFragment autoFragment = (AutoModeFragment) adapter.GetFragmentByPosition(0);
//        if (autoFragment != null) {
//            Log.d("autofragment","not null");
//            viewPlayedSound = autoFragment.getTextViewPlayedSoundInText();
//            editTextNumberSoundLength = autoFragment.getEditTextNumberSoundLength();
//        } else {
//
//            Log.d("autofragment","null");
//        }
//        ManualModeFragment manualModeFragment = (ManualModeFragment) getSupportFragmentManager()
//                .findFragmentByTag("f" + viewPager.getId() + ":1");
//
////        ManualModeFragment manualModeFragment = (ManualModeFragment) adapter.GetFragmentByPosition(1);
//        if (manualModeFragment != null) {
//            Log.d("manualfragment","not null");
//            problemET = manualModeFragment.getEditTextProblem();
//        }else {
//
//            Log.d("manualfragment","null");
//        }
        //END

//        //ADDING SELECTION LISTENER. WHEN USER SELECTS TAB, methods will be accessed
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    AutoModeFragment fragment = (AutoModeFragment) adapter.GetFragmentByPosition(0);
//                    if (fragment != null) {
//                        viewPlayedSound = fragment.getTextViewPlayedSoundInText();
//                        editTextNumberSoundLength = fragment.getEditTextNumberSoundLength();
//                    }
//                }
//                if (tab.getPosition() == 1) {
//                    ManualModeFragment manualModeFragment = (ManualModeFragment) adapter.GetFragmentByPosition(1);
//                    if (manualModeFragment != null) {
//                        problemET = manualModeFragment.getEditTextProblem();
//                    }
//                }
//            }
//
//            @Override public void onTabUnselected(TabLayout.Tab tab) {}
//            @Override public void onTabReselected(TabLayout.Tab tab) {}
//        });

//        AutoModeFragment fragment = (AutoModeFragment)  adapter.GetFragmentByPosition(0);

//        if (fragment != null) {
//            viewPlayedSound = fragment.getTextViewPlayedSoundInText(); // A method you define in the fragment
//            editTextNumberSoundLength = fragment.getEditTextNumberSoundLength();
//        }


        alreadyTried = false;
        problemAlreadyTried = "";
        db = new DBHandler(MainActivity.this);

        userSolution = (EditText) findViewById(R.id.editTextYourSolution);
//        userSolution.setImeHintLocales(new LocaleList(new Locale("sr", "RS", "Cyrl"))); ;

//        viewPlayedSound = (TextView)findViewById(R.id.textViewPlayedSoundInText);

        solutionToMyProblem = new LinkedList<>();

        latinAlphabet = new LinkedList<>();

        tcheck = (TextView)findViewById(R.id.textViewCheck);
        tsolution = (TextView)findViewById(R.id.textViewSolution2);

        setLetters();

        getSounds();
        mp = new LinkedList<>();
        r = new Random();


        chosenSounds = new LinkedList<>();
        solution = new LinkedList<>();


        sortedChosenDigitsMyProblem = new LinkedList<>();
        sortedChosenLettersMyProblem = new LinkedList<>();

        sortedChosenDigits = new LinkedList<>();
        sortedChosenLetters = new LinkedList<>();
//
//        editTextNumberSoundLength.setText(new Integer(3).toString());
        answersGV = findViewById(R.id.idGridViewAnswers);
        answers  = new ArrayList<>();

        playSounds = false;
        //DELETE I PLAY AGAIN DIJALOZI
        answersGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Answer an = (Answer)(answersGV.getItemAtPosition(position));

                String clickedText = answersGV.getItemAtPosition(position).toString();
                selectedAnswerID = Integer.parseInt((clickedText.split(" ")[0]));

                // TextView test = findViewById(R.id.textViewTest);
                //test.setText(Integer.toString(selectedAnswerID));

                String items[] = new String[3];
                items[0] = "Try again this problem";
                items[1] = "Delete";
                items[2] = "Cancel";

                AlertDialog.Builder dialog =  new AlertDialog.Builder(MainActivity.this)
                        .setTitle("What do you want to do with selected item?")
                        //.setMessage("Delete this entry?")
                        .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0) {

                                    // Log.d("test","on dialog try again click");
                                    alreadyTried = true;
                                    problemAlreadyTried =  clickedText.split(" ")[1];
                                    AddSoundsToMediaPlaylist(problemAlreadyTried);
                                    playSounds = true;

                                    PlaySound ps =  new PlaySound();
                                    ps.start();





                                } else {
                                    if (i == 1) {
                                        db.DeleteAnswer(selectedAnswerID);
                                        ReadAnswers(view);
                                    }
                                    playSounds = false;
                                }
                            }
                        });
                dialog.show();

            }

        });



        //SCROLL

        answersGV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        answersGV.scrollListBy(3);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        answersGV.scrollListBy(-3);
                        break;
                }

                // Handle ListView touch events.
                view.onTouchEvent(motionEvent);
                return true;
            }
        });


        AnswerGVAdapter answerAdapter = new AnswerGVAdapter(this, answers);
        answersGV.setAdapter(answerAdapter);

    //Start of OLD
//        problemET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                generated = false;
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        problemET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b) generated = false;
//                else generated = true;
//            }
//        });
            //END OF OLD

//
//        scaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            float scaleFactor = 1.0f;
//
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
//
//                Log.d("scaling", detector.toString());
//
//                scaleFactor *= detector.getScaleFactor();
//                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 3.0f)); // Clamp scale
//                findViewById(R.id.root_layout).setScaleX(scaleFactor);
//                findViewById(R.id.root_layout).setScaleY(scaleFactor);
//
//                return true;
//            }
//        });
//
//        findViewById(R.id.root_layout).setOnTouchListener((v, event) -> {
//            scaleDetector.onTouchEvent(event);
//
//            //gestureDetector.onTouchEvent(event);
//            return true;
//        });



//        getManualViews();
//        getGeneratedViews();
//        SetManualInvisible();

        TextView textView = findViewById(R.id.buttonReadFromDatabase);
        textView.setBackgroundResource(R.drawable.gradient_blue);

//        TextView textView = findViewById(R.id.buttonReadFromDatabase);
//        Paint paint = textView.getPaint();
//        float width = paint.measureText(textView.getText().toString());
//
//        Shader textShader = new LinearGradient(
//                0, 0, width, textView.getTextSize(),
//                new int[]{
//                        Color.parseColor("#F97C3C"),
//                        Color.parseColor("#FDB54E"),
//                        Color.parseColor("#64B678"),
//                        Color.parseColor("#478AEA"),
//                        Color.parseColor("#8446CC")
//                },
//                null,
//                Shader.TileMode.CLAMP
//        );
//
//        textView.getPaint().setShader(textShader);

    }

    public void onAutoModeReady(TextView tv, EditText et) {
        viewPlayedSound = tv;
        editTextNumberSoundLength = et;
       // viewPager.setCurrentItem(0, false);
         //        viewPager.setCurrentItem(1, false);
    }



    public void onManualModeReady(EditText et) {

        problemET = et;

        problemET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generated = false;
                userSolution.setText("");
                tcheck.setText("");
                tsolution.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        problemET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) generated = false;
                else generated = true;
            }
        });
        //viewPager.setCurrentItem(1, false);
        //        viewPager.setCurrentItem(1, false);
    }

//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        scaleDetector.onTouchEvent(event);
//        return true;
//    }


    //CHOOSE WHICH PROBLEM TO SOLVE, GENERATED OR MANUALLY TYPED by hiding and showing views
    //START

//    LinkedList<View> manualViews;
//    LinkedList<View> generatedViews;
//
//    private boolean manualProblemVisible = true;
//    private boolean generatedProblemVisible = true;
//
//    //MANUAL PROBLEM
//    public void SetManualInvisible(){
//        for(View v : manualViews){
//            v.setVisibility(View.GONE);
//        }
//        manualProblemVisible = false;
//    }
//
//    public void SetManualVisible(){
//        for(View v : manualViews){
//            v.setVisibility(View.VISIBLE);
//        }
//        manualProblemVisible = true;
//    }
////
//
//    public void ToggleProblems(View view){
//        if(manualProblemVisible) {
//            SetManualInvisible();
//            SetGeneratedVisible();
//        }
//
//        else  {
//            SetManualVisible();
//            SetGeneratedInvisible();
//        }
//    }
//
//
//    public void getManualViews(){
//        manualViews = new LinkedList<>();
//        manualViews.add(findViewById(R.id.editTextProblem));
//        manualViews.add(findViewById(R.id.buttonCheck2));
//        manualViews.add(findViewById(R.id.buttonCheckSolutionForUserProblem));
//
//
//    }
//    //GENERATED PROBLEM
//
//    public void getGeneratedViews(){
//        generatedViews = new LinkedList<>();
//        generatedViews.add(findViewById(R.id.textViewLengthLabel));
//        generatedViews.add(findViewById(R.id.editTextNumberSoundLength));
//        generatedViews.add(findViewById(R.id.buttonGenerate));
//        generatedViews.add(findViewById(R.id.buttonPlay));
//        generatedViews.add(findViewById(R.id.buttonShowPlayed));
//        generatedViews.add(findViewById(R.id.textViewPlayedSoundInText));
//
//    }
//
//
//    public void SetGeneratedInvisible(){
//        for(View v : generatedViews){
//            v.setVisibility(View.GONE);
//        }
//        generatedProblemVisible = false;
//    }
//
//    public void SetGeneratedVisible(){
//        for(View v : generatedViews){
//            v.setVisibility(View.VISIBLE);
//        }
//        generatedProblemVisible = true;
//    }

//    public void ToggleGeneratedProblem(View view){
//        if(generatedProblemVisible) SetGeneratedInvisible();
//        else SetGeneratedVisible();
//    }




    //CHOOSE WHICH PROBLEM TO SOLVE, GENERATED OR MANUALLY TYPED
    //END



    public void setLetters(){

        latinAlphabet.add(new Pair<>(0, "a"));
        latinAlphabet.add(new Pair<>(1, "b"));
        latinAlphabet.add(new Pair<>(2, "c"));
        latinAlphabet.add(new Pair<>(3, "d"));
        latinAlphabet.add(new Pair<>(4, "e"));
        latinAlphabet.add(new Pair<>(5, "f"));
        latinAlphabet.add(new Pair<>(6, "g"));
        latinAlphabet.add(new Pair<>(7, "h"));
        latinAlphabet.add(new Pair<>(8, "i"));
        latinAlphabet.add(new Pair<>(9, "j"));
        latinAlphabet.add(new Pair<>(10, "k"));
        latinAlphabet.add(new Pair<>(11, "l"));
        latinAlphabet.add(new Pair<>(12, "m"));
        latinAlphabet.add(new Pair<>(13, "n"));
        latinAlphabet.add(new Pair<>(14, "o"));
        latinAlphabet.add(new Pair<>(15, "p"));
        latinAlphabet.add(new Pair<>(16, "q"));
        latinAlphabet.add(new Pair<>(17, "r"));
        latinAlphabet.add(new Pair<>(18, "s"));
        latinAlphabet.add(new Pair<>(19, "t"));
        latinAlphabet.add(new Pair<>(20, "u"));
        latinAlphabet.add(new Pair<>(21, "v"));
        latinAlphabet.add(new Pair<>(22, "w"));
        latinAlphabet.add(new Pair<>(23, "x"));
        latinAlphabet.add(new Pair<>(24, "y"));
        latinAlphabet.add(new Pair<>(25, "z"));


    }
////    public String fromCyrillicTolatin(String letterCyrillic){
////        for (Triple t : latinToCyrillic ) {
////            if(t.getThird().equals(letterCyrillic)) return t.getSecond().toString();
////        }
////        return "";
////    }
//
    public Triple<Integer, String , Integer> findTripleLetter(String second){

        for (Triple t : lettersResources) {
            if(t.getSecond().equals(second)){
                Triple tCyr = new Triple(t.getFirst(), second, t.getThird());
                Log.d("second from triple", second);
                return tCyr;
            }
        }
        return null;
    }

////    public Pair findTripleLetterFromLettersByCyrillic(String cyrillic){
////        Pair withRid = new Pair("",0);
////        for (Triple t:
////                letters) {
////            if(t.getSecond().equals(fromCyrillicTolatin(cyrillic))){
////                withRid  = new Pair(cyrillic, (Integer)t.getThird());
////            }
////        }
////        return withRid;
////    }
//
    public Triple<Integer, String , Integer> findTripleDigit(String second){
        for (Triple t : digitsResources) {
            if(t.getSecond().equals(second)) return t;
        }
        return null;
    }
//
    public void ShowText(View view){
        String sounds = "";
        //String singleSounds =
        if(alreadyTried) {
            char[] soundsArray = problemAlreadyTried.toCharArray();

            for (int i = 0; i < soundsArray.length; i++) {
                sounds += soundsArray[i];
            }
        }
        else {
            for (Triple sound : chosenSounds) {
                //sounds+=sound.getSecond();
                Log.d("second sound", sound.getSecond().toString());
                if (digitsResources.contains(sound)) {

                    Log.d("second sound", sound.getSecond().toString());
                    sounds += sound.getSecond();
                } else {
                    // Log.d("sound", getCyrillicLetter((String) sound.getSecond()));
                    sounds += sound.getSecond().toString();//getCyrillicLetter((String) sound.getSecond());

                }
            }
        }
        TextView showText = (TextView) findViewById(R.id.textViewPlayedSoundInText);
        showText.setText(sounds);
    }
//
    public void AddSoundsToMediaPlaylist(String problem){

        mp.clear();


        String testS = "";
        char[] problemCharacters = problem.toCharArray();
        for (char letterOrDigit : problemCharacters) {
            testS += Character.toString(letterOrDigit);
            if(Character.isDigit(letterOrDigit)){
                // test.setText(Integer.toString(letterOrDigit))

                int rid = getSoundResourceIDFromDigit(( Character)(letterOrDigit));
//                test.setText(Integer.toString(rid));
                //test.setText(rid);
                mp.add(MediaPlayer.create(this, rid));
            }
            if(Character.isLetter(letterOrDigit)){
                //  test.setText("it's letter");
                int rid =(Integer) getSoundResourceIDFromLetter(Character.toString(letterOrDigit));
                mp.add(MediaPlayer.create(this, rid));
            }
        }
//
        // test.setText(testS);
    }


    public void sortDigits(LinkedList<Triple> digits){
        digits.sort( new Comparator<Triple>(){

            @Override
            public int compare(Triple triple, Triple t1) {
                if(Integer.parseInt((String)triple.getSecond()) < Integer.parseInt((String)t1.getSecond())){
                    return -1;
                }

                if(Integer.parseInt((String)triple.getSecond()) > Integer.parseInt((String)t1.getSecond())){
                    return 1;
                }
                return 0;
            }
        });
    }
//
    private Integer getRandomDigit() {
        int digit = r.nextInt(10);
        return digit;
    }

    private Integer getRandomLetter() {
        int letter = r.nextInt(26);
        return letter;
    }
//
////    public Triple<Integer, String, Integer> getLatinToCyrillicIndexByLatin(String latin){
////        for (Triple<Integer, String, Integer> t : latinToCyrillic){
////            if(t.getSecond().equals(latin)){
////                return t;
////            }
////        }
////        return null;
////    }
//
    public int getSoundResourceIDFromDigit(char digit){

//        TextView test = findViewById(R.id.textViewTest);

//        test.setText(Character.toString(digit));

        int rid = (Integer)(digitsResources.get(Integer.parseInt(Character.toString(digit))).getThird());
        return rid;
    }

    public int getSoundResourceIDFromLetter(String letter){
        for (int i = 0; i < lettersResources.size(); i++) {
            if((lettersResources.get(i).getSecond().toString()).equals(letter)) return (Integer) lettersResources.get(i).getThird();

        }

        return 0;
    }

    public int GetLatinLetterIndex(String soundName){
        for(int i = 0; i < latinAlphabet.size(); i++){
            if((latinAlphabet.get(i)).second.equals(soundName)) return i;
        }
        return -1;
    }
//
//
//    //DONE
    public void getSounds(){
        lettersResources = new LinkedList<>();
        digitsResources = new LinkedList<>();

        int digitIndex = 0;
        int letterIndex = 0;

        try {
            Field[] fields = R.raw.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                int rid = fields[i].getInt(fields[i]);
                String filename = fields[i].getName();


                String soundName =filename.replaceAll(".wav","");

                if(filename.contains("digit")) {
                    soundName = soundName.replace("digit_","");
                    Triple<Integer, String, Integer> triple = new Triple<>(digitIndex, soundName, rid); //rid je isto kao R.raw.filename. oba su tipa int.
                    digitsResources.add(triple);
                    digitIndex++;
                    // digits.add(rid);
                }
                if(filename.contains("letter")){
                    soundName = soundName.replace("letter_","");
                    Triple<Integer, String, Integer> triple = new Triple<>(GetLatinLetterIndex(soundName), soundName, rid);
                    lettersResources.add(triple);

                    //letters.add(rid);
                }
            }
        } catch (IllegalAccessException ex){
        }
        //sortDigits();
        //sortLetters();
    }

    public void sortLetters(LinkedList<Triple> letters) {
        Log.d("letters from problem:", letters.toString());
        letters.sort(new Comparator<Triple>() {
            @Override
            public int compare(Triple triple, Triple t1) {
                if((Integer)triple.getFirst() < (Integer)t1.getFirst()){
                    return -1;
                }
                if((Integer)triple.getFirst() > (Integer)t1.getFirst()){
                    return 1;
                }
                return 0;
            }
        });


    }



//    public String toCyrillic(String latin) {
//        String cyrillic = "";
//        for (int i = 0; i < latin.length(); i++) {
//            if(Character.isLetter(latin.charAt(i))) {
//                cyrillic += getCyrillicLetter(Character.toString(latin.charAt(i)));
//            }
//            else cyrillic += latin.charAt(i);
//        }
//        return cyrillic;
//    }


    public boolean SolveAlreadyTried(String problem){



        // Answer problemAndSolution = db.GetSolutionForProblem(problem);

        //String testS = problemAndSolution.toString();
        //test.setText(testS);


        EditText userSol = findViewById(R.id.editTextYourSolution);
        String userSolution = userSol.getText().toString();

        Solve();
        String solutionS = "";
        for (Triple t : solution) {

            Log.d("char", Character.toString(t.getSecond().toString().charAt(0)));

            if(Character.isLetter(t.getSecond().toString().charAt(0))) {
                Log.d("letter char", "" +t.getSecond().toString().charAt(0));
                solutionS += t.getSecond().toString();
            } else
            if(Character.isDigit(t.getSecond().toString().charAt(0))){
                Log.d("digit char", "" +t.getSecond().toString().charAt(0));
                solutionS += t.getSecond().toString();
            }
        }
        Log.d("solution", solutionS);

//        String cyrillicSolution = toCyrillic(solutionS);

        Answer userAnswer = new Answer(problem, solutionS, userSolution);
        // Answer userAnswer = new Answer(problemAndSolution.getSound(),problemAndSolution.getSolution(), userSolution);



        // userAnswer.setSolution(solution.toString());

        //Insert answer to nasumicna_slova_i_brojevi database

        db.AddAnswer(userAnswer);

        answers = db.GetAnswers();

        ArrayList<Answer> answersFromLastToFirst = new ArrayList<>();
        for (int i = answers.size()-1; i >= 0; i--) {
            answersFromLastToFirst.add(answers.get(i));
        }

        AnswerGVAdapter answerAdapter = new AnswerGVAdapter(this, answersFromLastToFirst);

        answersGV.setAdapter(answerAdapter);

        TextView isCorrect = (TextView)findViewById(R.id.textViewCheck);
        isCorrect.setText(userAnswer.checkAnswer());

        TextView solutionTextView = findViewById(R.id.textViewSolution2);
        solutionTextView.setText(solutionS);


        return userAnswer.isCorrect();

    }

//    public void SortChosenSounds(){
//        for (Triple t : chosenSounds) {
//            Log.d("character", t.getSecond().toString());
//            //if(Character.isLetter(t.getSecond().toString())){
//
//        }
//
//    }

    public void Solve(){
        solution.clear();


        sortDigits(sortedChosenDigits);
        sortLetters(sortedChosenLetters);

        for (Triple t : sortedChosenDigits) {
            solution.add(t);
        }

        for (Triple t : sortedChosenLetters) {
            solution.add(t);
        }
//        String solutionS = "";
//        for (Triple t:solution) {
//            solutionS += getCyrillicLetter(t.getSecond().toString());
//        }
//        Log.d("solution", solutionS);
    }



//    public boolean IsLetter(String sound){
//        for (Triple<Integer, String, Integer> t : latinToCyrillic) {
//            if(Character.isLetter(t.getSecond().charAt(0))) return true;
//        }
//        return false;
//    }

    public void CheckSolution(View view){

        if(userSolution.getText().toString().isEmpty()){


            String items[] = new String[3];
            items[0] = "Please type your solution";


            AlertDialog.Builder dialog =  new AlertDialog.Builder(MainActivity.this)
                    .setTitle("What do you want to do with selected item?")
                    //.setMessage("Delete this entry?")
                    .setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            dialog.show();
        }

        if(alreadyTried){
            chosenSounds.clear();
            sortedChosenDigits.clear();
            sortedChosenLetters.clear();
            char[] alreadyTriedSounds = problemAlreadyTried.toCharArray();
            for (int i = 0; i < alreadyTriedSounds.length; i++) {
                Triple<Integer, String, Integer> letter = findTripleLetter(Character.toString(alreadyTriedSounds[i]));
                Triple<Integer, String, Integer> digit = findTripleDigit(Character.toString(alreadyTriedSounds[i]));
                if(letter != null) {
                    sortedChosenLetters.add(letter);

                    chosenSounds.addLast(letter);
                }
                else
                if(digit != null) {
                    sortedChosenDigits.add((digit));

                    chosenSounds.addLast(digit);
                }
            }



            SolveAlreadyTried(problemAlreadyTried);
            return;
        }

        Solve();
        String solutionString = "";
        for (Triple t: solution) {
//            if(t.getSecond().toString().length() > 1){
//                solutionString += getCyrillicLetter((String)t.getSecond());
//            }
//            else {
//                if (t.getSecond().toString().length() == 1) {
//                        if (Character.isDigit(t.getSecond().toString().charAt(0))) {
//                            solutionString += t.getSecond().toString();
//                        } else solutionString += getCyrillicLetter(t.getSecond().toString());
//
//                }
//            }
            solutionString += t.getSecond().toString();
        }

        Answer userAnswer = new Answer(GetSoundString(), solutionString, userSolution.getText().toString());
        //answers.add(userAnswer);

        //Insert answer to nasumicna_slova_i_brojevi database

        db.AddAnswer(userAnswer);

        answers = db.GetAnswers();

        ArrayList<Answer> answersFromLastToFirst = new ArrayList<>();
        for (int i = answers.size()-1; i >= 0; i--) {
            answersFromLastToFirst.add(answers.get(i));
        }

        AnswerGVAdapter answerAdapter = new AnswerGVAdapter(this, answersFromLastToFirst);

        answersGV.setAdapter(answerAdapter);

        TextView isCorrect = (TextView)findViewById(R.id.textViewCheck);
        isCorrect.setText(userAnswer.checkAnswer());

        TextView solutionTextView = findViewById(R.id.textViewSolution2);
        solutionTextView.setText(solutionString);

    }

    public String GetSoundString(){
        sound = "";
        for (Triple t: chosenSounds ) {
            if (t.getSecond().toString().length() > 1) {
//                sound += getCyrillicLetter(t.getSecond().toString());
                sound += t.getSecond().toString();
            } else if (t.getSecond().toString().length() == 1) {

                if (Character.isDigit(t.getSecond().toString().charAt(0))) {
                    sound += t.getSecond().toString();
                } else {
//                    sound += getCyrillicLetter(t.getSecond().toString());
                    sound += t.getSecond().toString();
                }
            }

        }
        return sound;
    }

    public void ClearText(View view){
        EditText problem = findViewById(R.id.editTextProblem);
        problem.setText("");
    }

//    public int GetLetterCyrillicIndex(String cyrillicLetter){
//        for (Triple t : latinToCyrillic) {
//            if(getCyrillicLetter(t.getSecond().toString()).equals(cyrillicLetter)){
//                return (int)t.getFirst();
//            }
//        }
//        return -1;
//    }


    public void ReadAnswers(View view){
        ReadAnswers();
    }

    public void ReadAnswers(){
        answers.clear();
        answers = db.GetAnswers();

        ArrayList<Answer> answersFromLastToFirst = new ArrayList<>();
        for (int i = answers.size()-1; i >= 0; i--) {
            answersFromLastToFirst.add(answers.get(i));
        }

        AnswerGVAdapter answerAdapter = new AnswerGVAdapter(this, answersFromLastToFirst);
        answersGV.setAdapter(answerAdapter);
    }

    public void SolveMyProblem(){
        solutionToMyProblem.clear();
        sortDigits(sortedChosenDigitsMyProblem);
        sortLetters(sortedChosenLettersMyProblem);

        for (Triple t : sortedChosenDigitsMyProblem) {
            solutionToMyProblem.add(t);
        }

        for (Triple t : sortedChosenLettersMyProblem) {
            solutionToMyProblem.add(t);
        }
    }
    LinkedList<String> soundsMyProblem;

    public void SetMyProblem(){
        sortedChosenLettersMyProblem.clear();
        sortedChosenDigitsMyProblem.clear();
        String problem  = ((EditText)findViewById(R.id.editTextProblem)).getText().toString();
        soundsMyProblem = new LinkedList<>();
        for (int i = 0; i < problem.length(); i++) {
            Character c = (Character)problem.charAt(i);
            if(c ==' ') continue;
            String s = ((Character)problem.charAt(i)).toString();
            soundsMyProblem.add(s);
            if(Character.isDigit(c)){
                sortedChosenDigitsMyProblem.add(new Triple(c, c.toString(), 0));
            } else {
                sortedChosenLettersMyProblem.add(new Triple(GetLatinLetterIndex(c.toString()),c.toString(),0));
            }
        }
    }

    public void SetSolutionToMyProblem(){
        String solutionText = "";
        for (Triple t : solutionToMyProblem) {
            solutionText += t.getSecond();
        }
        TextView textViewSolution2 = (TextView)findViewById(R.id.textViewSolution2);
        textViewSolution2.setText(solutionText);
    }

    public void SolveMyProblem(View view){
        SetMyProblem();
        SolveMyProblem();
        SetSolutionToMyProblem();
    }



    public void CheckSolutionForUserProblem(View view){
        if(solutionToMyProblem.size() == 0) {
            SetMyProblem();
            SolveMyProblem();
            SetSolutionToMyProblem();
            //SolveMyProblem(null);
        }

        String solution = "";
        for (Triple t : solutionToMyProblem) {
            solution += t.getSecond().toString();
        }

        String userSolution = ((EditText)findViewById(R.id.editTextYourSolution)).getText().toString();
        String problem  = ((EditText)findViewById(R.id.editTextProblem)).getText().toString();

        Answer answer = new Answer(problem, solution, userSolution);

        db.AddAnswer(answer);
        ReadAnswers();

        TextView result = (TextView)findViewById(R.id.textViewCheck);
        result.setText(answer.checkAnswer());


        solutionToMyProblem.clear();
    }


    public void GenerateProblem(View view){
        GenerateProblem();

    }

    boolean generated = false;
    public void GenerateProblem(){
        generated = true;
        alreadyTried = false;



        userSolution.requestFocus();

        userSolution.setText("");
        tsolution.setText("");
        tcheck.setText("");

        sortedChosenDigits.clear();
        sortedChosenLetters.clear();
        chosenSounds.clear();


        String soundLength = editTextNumberSoundLength.getText().toString();
        Integer soundLengthInt = Integer.parseInt(soundLength);


        if(playSounds) return;

        mp.clear();
        for (int i = 0; i < soundLengthInt; i++) {
            boolean letter = r.nextBoolean();
            if(letter) {
                int chosenLetter = getRandomLetter();
                chosenSounds.add(new Triple(lettersResources.get(chosenLetter).getFirst(), lettersResources.get(chosenLetter).getSecond().toString(), lettersResources.get(chosenLetter).getThird()));


                sortedChosenLetters.add(new Triple(GetLatinLetterIndex(lettersResources.get(chosenLetter).getSecond().toString()), lettersResources.get(chosenLetter).getSecond().toString(), lettersResources.get(chosenLetter).getThird()));

                //Integer rid = (Integer)letters.get((int)let).second;
                Integer rid = (Integer) lettersResources.get(chosenLetter).getThird();
                // testText+="rid letter: " + rid.toString() + "\n";
                mp.add(MediaPlayer.create(this, rid));


            }
            if(!letter){
                int digit = getRandomDigit();
                chosenSounds.add(digitsResources.get(digit));

                //Integer rid = (Integer)digits.get((int)digit).second;

                Triple digitTriple = digitsResources.get(digit);
                //chosenDigits.add(digitTriple);

                sortedChosenDigits.add(digitsResources.get(digit));


                Integer rid = (Integer) digitsResources.get(digit).getThird();
                // testText+= "rid digit: " + rid.toString() + "\n";
                mp.add(MediaPlayer.create(this, rid));
            }
        }

    }

    public void AddMyProblem(){
        mp = null;
        mp = new LinkedList<>();
        for (String s: soundsMyProblem){
            Character c = s.charAt(0);
            if(Character.isLetter(c)) {
                int letter = GetLatinLetterIndex(s);
//                Log.d("letter", letter.getSecond().toString() + ", rid: " + letter.getThird().toString());
//                Log.d("rid", letter.getThird().toString());
                mp.add(MediaPlayer.create(this,  letter));
            }
            if(Character.isDigit(c)){
                mp.add(MediaPlayer.create(this, (Integer)findTripleDigit(s).getThird()));
            }

        }
    }


    public void Play(View view){

        EditText problem = problemET;
        if(!problem.getText().toString().isEmpty() && !generated && problem.isFocused()) {
            SetMyProblem();
            AddMyProblem();
            EditText userSolution = findViewById(R.id.editTextYourSolution);
            userSolution.requestFocus();
            userSolution.setText("");
        };

        playSounds = true;
        PlaySound ps = new PlaySound();
        ps.start();



        EditText userSolution = findViewById(R.id.editTextYourSolution);
        userSolution.requestFocus();

    }

    public void test(View view){
        Log.d("test", "test on text change");
    }

}