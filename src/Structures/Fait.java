package Structures;

public class Fait {
    public final int x, y;

    public Fait(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Fait)) return false;

        Fait fait = (Fait) o;

        return x == fait.x && y == fait.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "cruchesAetB("+x+","+y+")";
    }

}
