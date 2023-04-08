package Structures;

public interface Expression {

    boolean unifier(int value, Integer[] context);

    int eval(Integer[] context);

    boolean isConstant();
}
