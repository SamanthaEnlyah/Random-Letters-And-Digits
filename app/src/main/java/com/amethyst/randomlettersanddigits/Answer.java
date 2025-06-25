package com.amethyst.randomlettersanddigits;

public class Answer {
    String Sound;
    String Solution;
    String UserSolution;
    boolean IsCorrect;
    int id;

    public Answer(){

    }

    public Answer(int id){
        this.id = id;
    }

    public Answer(String Sound, String Solution, String UserSolution){
        this.Sound = Sound;
        this.Solution = Solution;
        this.UserSolution = UserSolution;
        IsCorrect = Solution.equals(UserSolution);
    }

    public Answer(int id, String Sound, String Solution, String UserSolution){
        this.id = id;
        this.Sound = Sound;
        this.Solution = Solution;
        this.UserSolution = UserSolution;
        IsCorrect = Solution.equals(UserSolution);
    }

    public int getId() {
        return id;
    }

    public void setSound(String sound) {
        Sound = sound;
    }

    public String getSound() {
        return Sound;
    }

    public String getSolution() {
        return Solution;
    }

    public String getUserSolution() {
        return UserSolution;
    }

    public boolean isCorrect(){ return IsCorrect; }

    public void setCorrect(boolean correct) {
        IsCorrect = correct;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSolution(String solution) {
        Solution = solution;
    }

    public void setUserSolution(String userSolution) {
        UserSolution = userSolution;
    }

    public String checkAnswer(){
        return IsCorrect?"Correct":"Wrong";
    }

    public String toString(){
        return  id + " " + getSound() + " " + getSolution() + " " + getUserSolution() + " " + isCorrect();
    }
}
