package Structures;

import java.util.Arrays;

public class Regle {

    private int num;
    private FaitExpression premisse;
    private FaitExpression conclusion;
    private Relation conditions[];
    private static int compteur;

    public static void startCompteur() {
        compteur = 0;
    }

    public static int getCompteur() {
        return compteur;
    }


    public FaitDeduit declanche(FaitDeduit premisse, Integer[] context) {
        compteur++;
        if(!this.premisse.unifier(premisse, context))
            return null;
        for(Relation condition : conditions) {
            if(!condition.test(context)) return null;
        }
        return new FaitDeduit(conclusion.eval(context), this, premisse);
    }
    
    public int getNum() {
        return num;
    }

    public Regle(int num, FaitExpression premisse, FaitExpression conclusion, Relation[] conditions) {
        this.num = num;
        this.premisse = premisse;
        this.conclusion = conclusion;
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return describe() + "\n";
    }

    public String describe() {
        return "r"+num+": "+premisse+ Arrays.stream(conditions).map(condition->" et "+condition).reduce(String::concat).orElse("")+" alors "+conclusion;
    }

}
