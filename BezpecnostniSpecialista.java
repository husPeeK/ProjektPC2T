package projekt.firma;


import java.util.*;

public class BezpecnostniSpecialista extends Zamestnanec {
    public BezpecnostniSpecialista(int id, String jmeno, String prijmeni, int rok) {
        super(id, jmeno, prijmeni, rok);
    }

    @Override
    public void provedDovednost(Map<Integer, Zamestnanec> vsichni) {
        double skore = spolupracovnici.size() * 1.5; 
        System.out.println("Rizikové skóre specialisty " + getPrijmeni() + " je: " + skore);
    }
}