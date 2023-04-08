package Structures;

public class Relation {
    private final Expression a, b;
    private final Operation operation;

    public Relation(Expression a, Expression b, Operation operation) {
        this.a = a;
        this.b = b;
        this.operation = operation;
    }

    boolean test(Integer[] context) {
        int a = this.a.eval(context), b = this.b.eval(context);
        switch (operation) {
            case eq: return a==b;
            case ge: return a>=b;
            case gt: return a>b;
            case le: return a<=b;
            case lt: return a<b;
            case neq: return a!=b;
        }
        return false;
    }

    @Override
    public String toString() {
        return a+getOp()+b;
    }

    private String getOp() {
        switch (operation) {
            case eq: return "=";
            case ge: return ">=";
            case gt: return ">";
            case le: return "<=";
            case lt: return "<";
            case neq: return "!=";
        }
        return null;
    }
}
