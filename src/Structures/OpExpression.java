package Structures;

public class OpExpression implements Expression {
    private final Expression a, b;
    private final boolean isadd;

    public OpExpression(Expression a, Expression b, boolean isadd) {
        this.a = a;
        this.b = b;
        this.isadd = isadd;
    }

    @Override
    public boolean unifier(int x, Integer[] context) {
        if(a.isConstant()) return b.unifier(isadd?x-a.eval(null):a.eval(null)-x, context);
        if(b.isConstant()) return a.unifier(isadd?x-b.eval(null):b.eval(null)+x, context);
        return false;
    }

    @Override
    public int eval(Integer[] context) {
        return isadd?a.eval(context)+b.eval(context):a.eval(context)-b.eval(context);
    }

    @Override
    public boolean isConstant() {
        return a.isConstant() && b.isConstant();
    }

    @Override
    public String toString() {
        if(isadd) return a+"+"+b;
        String bString = ""+b;
        if(bString.contains("+") || bString.contains("-")) bString = "("+bString+")";
        return a+"-"+bString;
    }
}
