package projekt.firma;


import java.util.*;

public class DatovyAnalytik extends Zamestnanec {
    public DatovyAnalytik(int id, String jmeno, String prijmeni, int rok) {
        super(id, jmeno, prijmeni, rok);
    }

    @Override
    public void provedDovednost(Map<Integer, Zamestnanec> vsichni) {
        System.out.println("Analýza společných známých pro: " + getPrijmeni());
        
    }
}