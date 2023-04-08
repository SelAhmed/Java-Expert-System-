package systemeexpert;


import Structures.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class LectureInfo {

    private static final String NUM = "num", PREMISSE ="prem", CONDITION="cond", CONCLUSION="conc";
    private static final Pattern Regle_Pattern = Pattern.compile("r(?<"+NUM+">[0-9]+)\\s*:\\s*si\\s+");
    private static final Pattern ExpFait_Pattern = Pattern.compile("cruchesAetB\\s*\\(\\s*");
    private static final Pattern Fait_Pattern = Pattern.compile("cruchesAetB\\s*\\(\\s*(?<a>[0-9]+)\\s*,\\s*(?<b>[0-9]+)\\s*\\)\\s*(,|$)");
    private static final Pattern Empty_Pattern = Pattern.compile("\\s*");
    private static final Pattern Number_Pattern = Pattern.compile("([0-9]+)\\s*");
    private static final Pattern Alors_Pattern = Pattern.compile("alors\\s+");

    private List<Regle> regles;
    private Map<Fait, FaitDeduit> Faits;

    public List<Regle> getRegles() {
        return regles;
    }

    public Map<Fait, FaitDeduit> getFaits() {
        return Faits;
    }


    public LectureInfo() {
        this.regles = new LinkedList<>();
        this.Faits = new HashMap<>();
    }

    private VariableExpression x = new VariableExpression(0), y = new VariableExpression(1);

    private Matcher parseWithPattern(Pattern pattern, String dataString, int next[]) throws Exception {
        Matcher matcher = pattern.matcher(dataString);
        if (!matcher.find(next[0]) || matcher.start() != next[0]) {
            throw new Exception();
        }
        next[0] = matcher.end();
        return matcher;
    }

    private Regle parseRegle(String dataString) throws Exception {
        int next[] = {0};
        Matcher matcher = parseWithPattern(Regle_Pattern, dataString, next);
        int num_regle = Integer.parseInt(matcher.group(NUM));
        FaitExpression premisse = parseFaitExpression(dataString, next);
        List<Relation> conditions = new LinkedList<>();
        while(dataString.charAt(next[0]) == 'e') {
            next[0]++;
            if(dataString.charAt(next[0]++) != 't') throw new Exception();
            parseWithPattern(Empty_Pattern, dataString, next);
            conditions.add(parseRelation(dataString, next));
        }
        parseWithPattern(Alors_Pattern, dataString, next);
        FaitExpression conclusion = parseFaitExpression(dataString, next);
        return new Regle(num_regle, premisse, conclusion, conditions.stream().toArray(Relation[]::new));
    }

    private Relation parseRelation(String dataString, int[] next) throws Exception {
        Expression a, b;
        Operation operation;
        a = parseExpression(dataString, next);
        if(dataString.charAt(next[0]) == '<') {
            next[0]++;
            if(dataString.charAt(next[0]) == '=') {
                next[0]++;
                operation = Operation.le;
            } else operation = Operation.lt;
        } else if(dataString.charAt(next[0]) == '>') {
            next[0]++;
            if(dataString.charAt(next[0]) == '=') {
                next[0]++;
                operation = Operation.ge;
            } else operation = Operation.gt;
        } else if(dataString.charAt(next[0]) == '!') {
            next[0]++;
            if(dataString.charAt(next[0]) == '=') {
                next[0]++;
                operation = Operation.neq;
            } else throw new Exception();
        } else if(dataString.charAt(next[0]) == '=') {
            next[0]++;
            operation = Operation.eq;
        } else throw new Exception();
        parseWithPattern(Empty_Pattern, dataString, next);
        b = parseExpression(dataString, next);
        return new Relation(a, b, operation);
    }

    public FaitExpression parseFaitExpression(String dataString, int[] next) throws Exception {
        parseWithPattern(ExpFait_Pattern, dataString, next);
        Expression a, b;
        a = parseExpression(dataString, next);
        if(dataString.charAt(next[0]++)!=',') throw new Exception();
        parseWithPattern(Empty_Pattern, dataString, next);
        b = parseExpression(dataString, next);
        if(dataString.charAt(next[0]++)!=')') throw new Exception();
        parseWithPattern(Empty_Pattern, dataString, next);
        return new FaitExpression(a, b);
    }

    private Expression parseExpression(String dataString, int[] next) throws Exception {
        Expression expression = parseExpressionUnique(dataString, next);
        char c = dataString.charAt(next[0]);
        while(c == '+' || c == '-') {
            next[0]++;
            parseWithPattern(Empty_Pattern, dataString, next);
            expression = new OpExpression(expression, parseExpressionUnique(dataString, next), c=='+');
            c = dataString.charAt(next[0]);
        }
        return expression;
    }
    private Expression parseExpressionUnique(String dataString, int[] next) throws Exception {
        if(dataString.charAt(next[0]) == '(') {
            next[0]++;
            Expression expression = parseExpression(dataString, next);
            if (dataString.charAt(next[0]++) != ')') throw new Exception();
            parseWithPattern(Empty_Pattern, dataString, next);
            return expression;
        }
        if(dataString.charAt(next[0]) == '?') {
            next[0]++;
            Expression variable;
            char c = dataString.charAt(next[0]);
            if (c == 'x' || c == 'n') variable = x;
            else if (c == 'y' || c == 'p') variable = y;
            else throw new Exception();
            next[0]++;
            parseWithPattern(Empty_Pattern, dataString, next);
            return variable;
        }
        return new ConstantExpression(parseNumber(dataString, next));
    }

    private int parseNumber(String dataString, int[] next) throws Exception {
        Matcher matcher = parseWithPattern(Number_Pattern, dataString, next);
        return Integer.parseInt(matcher.group(1));
    }

    public void rempRegles(File file) throws IOException {
        Regle r;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String ligne;
            int num_ligne = 1;
            while ((ligne = br.readLine()) != null) {
                try {
                    r = parseRegle(ligne);
                } catch (Exception e) {
                    throw new IOException("Erreur Syntaxique dans le fichier de règle, ligne: "+num_ligne);
                }
                regles.add(r);
                num_ligne++;
            }
        }
    }

    public void lectureRegles() throws IOException {
        System.out.println(regles);
    }

    public void rempFaits(File file) throws IOException {
        FaitDeduit f;
        Scanner sc = new Scanner(file, "UTF-8");
        try {
            //noinspection InfiniteLoopStatement
            while(true) {
                String stringData = sc.next(Fait_Pattern);
                Fait fait = parseFait(stringData);
                Faits.putIfAbsent(fait, new FaitDeduit(fait, null, null));
            }
        } catch (NoSuchElementException e) {
            // loop end
        }
    }

    public Fait parseFait(String stringData) {
        Matcher matcher = Fait_Pattern.matcher(stringData);
        if(!matcher.find()) return null;
        return new Fait(Integer.parseInt(matcher.group("a")), Integer.parseInt(matcher.group("b")));
    }

    public void lectureFaits() throws IOException {
        for(Fait fait:Faits.values()) {
            System.out.print(fait+" ");
        }
    }

}
