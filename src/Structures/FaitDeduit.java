package Structures;

public class FaitDeduit extends Fait {

    private final FaitDeduit premisse;
    public final Regle regle;
    public final int depth;

    public FaitDeduit(Fait conclusion, Regle regle, FaitDeduit premisse) {
        super(conclusion.x, conclusion.y);
        this.premisse = premisse;
        this.regle = regle;
        if(regle==null) depth = 0;
        else depth = 1 + premisse.depth;
    }

    public String describe() {
        String fait = toString();
        if(regle == null) return "On a "+fait;
        return premisse.describe()+", "+regle+"Donc "+fait;
    }
}