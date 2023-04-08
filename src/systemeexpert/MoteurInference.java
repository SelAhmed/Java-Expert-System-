
package systemeexpert;

import Structures.Fait;
import Structures.FaitDeduit;
import Structures.FaitExpression;
import Structures.Regle;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("ALL")
public class MoteurInference {

    private List<Regle> regles;
    private Map<Fait, FaitDeduit> faits;
    private Map<String, Integer> meta = new HashMap<>();

    public MoteurInference(LectureInfo li) {
        faits = li.getFaits();
        regles = li.getRegles();
    }

    public FaitDeduit profondeur(FaitExpression but) {
        for(FaitDeduit actual: faits.values()) {
            FaitDeduit faitDeduit = profondeur(actual, but, new HashSet<>());
            if(faitDeduit!=null) return faitDeduit;
        }
        return null;
    }

    private FaitDeduit profondeur(FaitDeduit actual, FaitExpression but, Set<Fait> visite) {
        for(Regle regle: regles) {
            FaitDeduit faitDeduit = regle.declanche(actual, new Integer[]{null, null});
            if(faitDeduit==null || !visite.add(faitDeduit)) continue;
            if(but.unifier(faitDeduit, new Integer[]{null, null})) return faitDeduit;
            faitDeduit = profondeur(faitDeduit, but, visite);
            if(faitDeduit != null) return faitDeduit;
        }
        return null;
    }

    public FaitDeduit largeur(FaitExpression but) {
        for(FaitDeduit actual: faits.values()) {
            FaitDeduit faitDeduit = largeur(actual, but);
            if(faitDeduit!=null) return faitDeduit;
        }
        return null;
    }

    private FaitDeduit largeur(FaitDeduit actual, FaitExpression but) {
        Set<Fait> visite = new HashSet<>();
        List<FaitDeduit> reste = new LinkedList<>();
        reste.add(actual);
        visite.add(actual);
        while(!reste.isEmpty()) {
            actual = reste.remove(0);
            for(Regle regle: regles) {
                FaitDeduit faitDeduit = regle.declanche(actual, new Integer[]{null, null});
                if(faitDeduit==null || !visite.add(faitDeduit)) continue;
                if(but.unifier(faitDeduit, new Integer[]{null, null})) return faitDeduit;
                reste.add(faitDeduit);
            }
        }
        return null;
    }

    private ToIntFunction<Fait> heutistique = fait->fait.x==2?0:fait.x>0?7:fait.y>2?3:1;
    public FaitDeduit heuristique(FaitExpression but) {
        for(FaitDeduit actual: faits.values()) {
            FaitDeduit faitDeduit = heuristique(actual, but, heutistique);
            if(faitDeduit!=null) return faitDeduit;
        }
        return null;
    }

    private FaitDeduit heuristique(FaitDeduit actual, FaitExpression but, ToIntFunction<Fait> heutistique) {
        Comparator<FaitDeduit> comparator = (FaitDeduit f1, FaitDeduit f2) -> f2.depth-f1.depth+heutistique.applyAsInt(f2)-heutistique.applyAsInt(f1);
        TreeSet<FaitDeduit> reste = new TreeSet<FaitDeduit>(comparator);
        Set<Fait> visite = new HashSet<>();
        reste.add(actual);
        while(!reste.isEmpty()) {
            actual = reste.first();
            reste.remove(actual);
            visite.add(actual);
            for(Regle regle: regles) {
                FaitDeduit faitDeduit = regle.declanche(actual, new Integer[]{null, null});
                if(faitDeduit==null || visite.contains(faitDeduit)) continue;
                FaitDeduit old = reste.ceiling(faitDeduit);
                if(faitDeduit.equals(old)) {
                    if(comparator.compare(old, faitDeduit)>=0) continue;
                } else {
                    if(but.unifier(faitDeduit, new Integer[]{null, null})) return faitDeduit;
                }
                reste.add(faitDeduit);
            }
        }
        return null;
    }
}
