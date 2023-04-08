package systemeexpert;

import Structures.*;

import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;

import java.util.Scanner;

public class SystemeExpert {

    private static File f1, f2;

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) throws IOException {

        System.out.println("\n\n~~~~~~~~~~~~~~~~~~ BIENVENUE ~~~~~~~~~~~~~~~~~~\n\n");

        do {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Veuillez saisir le chemin complet de la base de règles:");
                String br = sc.nextLine();
                f1 = new File(ClassLoader.getSystemClassLoader().getResource(br).toURI());
                System.out.println("Veuillez saisir le chemin complet de la base de faits:");
                String bf = sc.nextLine();
                f2 = new File(ClassLoader.getSystemClassLoader().getResource(bf).toURI());
            } catch (Exception ex) {
                System.out.println("Fichier introuvable!! Veuillez réessayer");
            }
        } while (f1 == null && f2 == null);

        LectureInfo li = new LectureInfo();
        li.rempRegles(f1);
        li.rempFaits(f2);
        MoteurInference mi = new MoteurInference(li);

        do {

            System.out.println("\n\n~~~~~~~~~~~~~~~~~~ MENU SYSTEME EXPERT ~~~~~~~~~~~~~~~~~~\n\n");
            System.out.println("Veuillez taper votre choix: \n\n");
            System.out.println("1: Afficher la Base de Règles\n");
            System.out.println("2: Afficher la Base de faits\n");
            System.out.println("3: Chercher un but en profondeur\n");
            System.out.println("4: Chercher un but en largeur\n");
            System.out.println("5: Chercher un but par heuristique\n");
            System.out.println("0: Quitter\n");

            Scanner sc = new Scanner(System.in);
            String choix = sc.nextLine();

            switch (choix) {

                case "1": {
                    li.lectureRegles();
                    break;
                }
                case "2": {
                    li.lectureFaits();
                    break;
                }
                case "3": {
                    FaitExpression but = saisirBut("** En Profondeur **", li);
                    decrire(mi.profondeur(but), but);
                    break;
                }
                case "4": {
                    FaitExpression but = saisirBut("** En Largeur **", li);
                    decrire(mi.largeur(but), but);
                    break;
                }
                case "5": {
                    FaitExpression but = saisirBut("** Par Heuristique **", li);
                    decrire(mi.heuristique(but), but);
                    break;
                }
                case "0": {
                    exit(0);
                    break;
                }
                default: {
                    System.out.println("Choix invalide! Veuillez réessayer SVP.");
                    break;
                }
            }

        } while (true);

    }

    private static FaitExpression saisirBut(String titre, LectureInfo li) {
        System.out.println(titre);
        Regle.startCompteur();
        while(true) {
            System.out.print("Veuillez saisir votre but: ");
            try {
                return li.parseFaitExpression(new Scanner(System.in).nextLine().trim(), new int[]{0});
            } catch (Exception ignored) {}
        }
    }

    private static void decrire(FaitDeduit fait, FaitExpression but) {
        if(fait == null) {
            System.out.println("impossible de démontrer le but: "+but);
        } else {
            System.out.println("\n"+fait.describe());
            System.out.println("\nDémonstration faite en "+Regle.getCompteur()+" étapes");
        }
    }
}

