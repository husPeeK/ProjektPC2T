package projekt.firma;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SpravaFirmy sprava = new SpravaFirmy();
        
        sprava.nactiZSql();

        int volba = -1;
        System.out.println("=== SYSTÉM PRO SPRÁVU FIRMY ===");

        while (volba != 0) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Přidat zaměstnance");
            System.out.println("2. Přidat spolupráci");
            System.out.println("3. Odstranit zaměstnance");
            System.out.println("4. Detail zaměstnance");
            System.out.println("5. Abecední výpis");
            System.out.println("6. Statistiky");
            System.out.println("7. Počty ve skupinách");
            System.out.println("8. Export do TXT");
            System.out.println("9. Import z TXT");
            System.out.println("0. Konec a SQL záloha");
            System.out.print("Vyberte akci: ");

            try {
                volba = Integer.parseInt(sc.nextLine());
                switch (volba) {
                    case 1:
                        System.out.print("Typ (A-Analytik / S-Specialista): ");
                        String t = sc.nextLine().trim().toUpperCase();
                        while (!t.equals("A") && !t.equals("S")) {
                            System.out.print("Zadejte pouze A nebo S: ");
                            t = sc.nextLine().trim().toUpperCase();
                        }
                        String jm, pr;
                        while (true) {
                            System.out.print("Jméno: "); jm = sc.nextLine().trim();
                            if (jm.matches("^[a-zA-Zá-žÁ-Ž ]+$")) break;
                            System.out.println("Chyba: Používejte pouze písmena!");
                        }
                        while (true) {
                            System.out.print("Příjmení: "); pr = sc.nextLine().trim();
                            if (pr.matches("^[a-zA-Zá-žÁ-Ž ]+$")) break;
                            System.out.println("Chyba: Používejte pouze písmena!");
                        }
                        System.out.print("Rok narození: ");
                        int r = Integer.parseInt(sc.nextLine());
                        sprava.pridej(t, jm, pr, r);
                        break;
                    case 2:
                        System.out.print("ID prvního kolegy: "); int id1 = Integer.parseInt(sc.nextLine());
                        System.out.print("ID druhého kolegy: "); int id2 = Integer.parseInt(sc.nextLine());
                        System.out.print("Úroveň spolupráce (1-10): "); int ur = Integer.parseInt(sc.nextLine());
                        sprava.pridejVazbu(id1, id2, ur);
                        break;
                    case 3:
                        System.out.print("Zadejte ID k smazání: ");
                        sprava.odstran(Integer.parseInt(sc.nextLine()));
                        break;
                    case 4:
                        System.out.print("Zadejte ID pro detail: ");
                        sprava.najdi(Integer.parseInt(sc.nextLine()));
                        break;
                    case 5: sprava.vypisAbecedne(); break;
                    case 6: sprava.vypisStatistiky(); break;
                    case 7: sprava.vypisPocty(); break;
                    case 8: sprava.ulozDoSouboru("data.txt"); break;
                    case 9: sprava.nactiZeSouboru("data.txt"); break;
                    case 0: 
                        sprava.ulozDoSql(); 
                        System.out.println("Program se ukončuje...");
                        break;
                    default:
                        System.out.println("Neplatná volba.");
                }
            } catch (Exception e) { 
                System.out.println("CHYBA: Zadejte platné údaje!"); 
            }
        }
        sc.close();
    }
}