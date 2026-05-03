package projekt.firma;

import java.util.*;
import java.io.*;
import java.sql.*;

public class SpravaFirmy {
    private Map<Integer, Zamestnanec> databaze = new HashMap<>();
    private int citacId = 1;

    public void pridej(String typ, String jmeno, String prijmeni, int rok) {
        if (!typ.equalsIgnoreCase("A") && !typ.equalsIgnoreCase("S")) return;
        Zamestnanec z = typ.equalsIgnoreCase("A") ? 
            new DatovyAnalytik(citacId++, jmeno, prijmeni, rok) : 
            new BezpecnostniSpecialista(citacId++, jmeno, prijmeni, rok);
        databaze.put(z.getId(), z);
        System.out.println("Zaměstnanec přidán s ID: " + (citacId - 1));
    }

    public void pridejVazbu(int id1, int id2, int uroven) {
        Zamestnanec z1 = databaze.get(id1);
        Zamestnanec z2 = databaze.get(id2);
        if (z1 != null && z2 != null && id1 != id2) {
            z1.pridejSpolupraci(id2, uroven);
            z2.pridejSpolupraci(id1, uroven);
            System.out.println("Spolupráce nastavena.");
        } else System.out.println("Chyba: Neplatná ID.");
    }

    public void odstran(int id) {
        if (databaze.containsKey(id)) {
            databaze.remove(id);
            for (Zamestnanec z : databaze.values()) {
                z.getSpolupracovnici().remove(id);
            }
            System.out.println("Zaměstnanec odstraněn.");
        } else System.out.println("ID neexistuje.");
    }

    public void najdi(int id) {
        Zamestnanec z = databaze.get(id);
        if (z != null) {
            System.out.println("ID: " + z.getId() + " | " + z.getJmeno() + " " + z.getPrijmeni() + " (" + z.getRokNarozeni() + ")");
            System.out.println("Počet vazeb: " + z.getSpolupracovnici().size());
        } else System.out.println("Nenalezeno.");
    }

    public void vypisAbecedne() {
        if (databaze.isEmpty()) {
            System.out.println("Databáze je prázdná.");
            return;
        }
        List<Zamestnanec> list = new ArrayList<>(databaze.values());
        list.sort(Comparator.comparing(Zamestnanec::getPrijmeni));
        System.out.println("\n--- Abecední seznam ---");
        for (Zamestnanec z : list) {
            System.out.println(z.getPrijmeni() + " " + z.getJmeno() + " (ID: " + z.getId() + ")");
        }
    }

    public void vypisStatistiky() {
        if (databaze.isEmpty()) return;
        Zamestnanec max = Collections.max(databaze.values(), Comparator.comparing(z -> z.getSpolupracovnici().size()));
        Map<Integer, Integer> counts = new HashMap<>();
        for (Zamestnanec z : databaze.values()) {
            for (int k : z.getSpolupracovnici().values()) {
                counts.put(k, counts.getOrDefault(k, 0) + 1);
            }
        }
        Integer topK = counts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(0);
        System.out.println("Nejvíce vazeb: " + max.getPrijmeni() + " (vazeb: " + max.getSpolupracovnici().size() + ")");
        System.out.println("Převažující úroveň spolupráce: " + topK);
    }

    public void vypisPocty() {
        long a = databaze.values().stream().filter(z -> z instanceof DatovyAnalytik).count();
        long s = databaze.values().stream().filter(z -> z instanceof BezpecnostniSpecialista).count();
        System.out.println("Analytici: " + a + " | Specialisté: " + s);
    }

    public void ulozDoSouboru(String f) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (Zamestnanec z : databaze.values()) {
                pw.println(z.getId() + ";" + z.getJmeno() + ";" + z.getPrijmeni() + ";" + z.getRokNarozeni() + ";" + (z instanceof DatovyAnalytik ? "A" : "S"));
            }
            System.out.println("Export do TXT hotov.");
        } catch (IOException e) { System.out.println("Chyba zápisu."); }
    }

    public void nactiZeSouboru(String f) {
        try (Scanner fs = new Scanner(new File(f))) {
            while (fs.hasNextLine()) {
                String[] p = fs.nextLine().split(";");
                pridej(p[4], p[1], p[2], Integer.parseInt(p[3]));
            }
            System.out.println("Načteno z TXT.");
        } catch (Exception e) { System.out.println("Soubor TXT nenalezen."); }
    }

    public void ulozDoSql() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:firma.db")) {
            Statement st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS zamestnanci (id INTEGER, jmeno TEXT, prijmeni TEXT, rok INTEGER, typ TEXT)");
            st.execute("DELETE FROM zamestnanci");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO zamestnanci VALUES (?, ?, ?, ?, ?)");
            for (Zamestnanec z : databaze.values()) {
                ps.setInt(1, z.getId()); ps.setString(2, z.getJmeno()); ps.setString(3, z.getPrijmeni());
                ps.setInt(4, z.getRokNarozeni()); ps.setString(5, z instanceof DatovyAnalytik ? "A" : "S");
                ps.executeUpdate();
            }
            System.out.println(">>> SQL ZÁLOHA PROVEDENA <<<");
        } catch (Exception e) { System.out.println("SQL Chyba: " + e.getMessage()); }
    }

    public void nactiZSql() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:firma.db")) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM zamestnanci");
            while (rs.next()) {
                pridej(rs.getString("typ"), rs.getString("jmeno"), rs.getString("prijmeni"), rs.getInt("rok"));
            }
            System.out.println(">>> DATA NAČTENA Z SQL <<<");
        } catch (Exception e) { System.out.println("Záloha nenalezena."); }
    }
}