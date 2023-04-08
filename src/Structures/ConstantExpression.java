package Structures;

public class ConstantExpression implements Expression {
    private final int value;

    public ConstantExpression(int value) {
        this.value = value;
    }

    @Override
    public boolean unifier(int x, Integer[] context) {
        return x == value;
    }

    @Override
    public int eval(Integer[] context) {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public String toString() {
        return ""+value;
    }
}
