package projekt.firma;

import java.util.*;

public abstract class Zamestnanec {
    private int id;
    private String jmeno;
    private String prijmeni;
    private int rokNarozeni;
    protected Map<Integer, Integer> spolupracovnici = new HashMap<>();

    public Zamestnanec(int id, String jmeno, String prijmeni, int rokNarozeni) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
    }

    public abstract void provedDovednost(Map<Integer, Zamestnanec> vsichni);

   
    public int getId() { return id; }
    public String getJmeno() { return jmeno; }
    public String getPrijmeni() { return prijmeni; }
    public int getRokNarozeni() { return rokNarozeni; }
    public Map<Integer, Integer> getSpolupracovnici() { return spolupracovnici; }

    public void pridejSpolupraci(int idKolegy, int uroven) {
        spolupracovnici.put(idKolegy, uroven);
    }
}