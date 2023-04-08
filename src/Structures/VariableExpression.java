package Structures;

public class VariableExpression implements Expression {
    private final int index;

    public VariableExpression(int index) {
        this.index = index;
    }

    @Override
    public boolean unifier(int x, Integer[] context) {
        if(context[index]!=null) return context[index]==x;
        context[index] = x;
        return true;
    }

    @Override
    public int eval(Integer[] context) {
        return context[index];
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    public String toString() {
        return index==0?"?x":"?y";
    }
}
