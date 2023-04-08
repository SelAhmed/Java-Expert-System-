package Structures;

@SuppressWarnings("WeakerAccess")
public class FaitExpression {
    public final Expression x, y;

    public FaitExpression(Expression x, Expression y) {
        this.x = x;
        this.y = y;
    }

    public boolean unifier(Fait fait, Integer[] context) {
        return x.unifier(fait.x, context) && y.unifier(fait.y, context);
    }

    public Fait eval(Integer[] context) {
        return new Fait(x.eval(context), y.eval(context));
    }
    @Override
    public String toString() {
        return "cruchesAetB("+x+","+y+")";
    }
}
