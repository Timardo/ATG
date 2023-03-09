package grafyatg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 
 * @author Timardo
 *
 */
public class LabelSet {

    /**
     * Hodnota "nekonečno"
     */
    public static final int MAX_VALUE = Integer.MAX_VALUE;
    private final Graf graf;
    
    public LabelSet(GrafEnum typGrafu) {
        this.graf = typGrafu.nacitajGraf();
    }

    /**
     * Jednoduchá implementácia LabelSet algoritmu, ktorá používa dostupnú
     * dátovú štruktúru z {@link Graf}u. Cestu aj jej dĺžku priamo vypíše.
     * 
     * @param vrcholZ - začiatočný vrchol
     * @param vrcholDo - konečný vrchol
     */
    public void najdiVzdialenost(int vrcholZ, int vrcholDo) {
        // iniciálizácia premenných
        int[] vzdialenosti = new int[this.graf.getPocetVrcholov()];
        int[] indexyVrcholov = new int[this.graf.getPocetVrcholov()];
        ArrayList<Integer> epsilon = new ArrayList<Integer>(); // množina epsilon
        int riadiaciVrchol = 0;
        // pomocná premenná pre hľadanie vrcholu s najkratšou cestou v množine epsilon
        int min = MAX_VALUE;
        
        // inicializácia polí vzdialeností a indexov vrcholov
        for (int i = 0; i < vzdialenosti.length; i++) {
            vzdialenosti[i] = MAX_VALUE;
            indexyVrcholov[i] = -1;
        }
        
        vzdialenosti[vrcholZ] = 0;
        
        epsilon.add(vrcholZ);
        
        long casovac = System.nanoTime();
        
        while (!epsilon.isEmpty()) {
            // hľadanie vrcholu s najkratšou cestou v epsilone
            for (int i : epsilon) {
                if (vzdialenosti[i] < min) {
                    min = vzdialenosti[i];
                    riadiaciVrchol = i;
                }
            }
            
            min = MAX_VALUE; // resetovanie
            epsilon.remove((Integer)riadiaciVrchol); // odstráni vrchol z epsilonu
            
            // prechádzanie hrán
            for (Hrana hrana : this.graf.getZoznamHran()) {
                // ak vrchol Z nie je rovný s riadiacim - preskočiť
                if (hrana.getVrcholZ() != riadiaciVrchol) {
                    continue;
                }
                
                // uplatnenie podmienky zmeny min. dĺžky
                if (vzdialenosti[hrana.getVrcholDo()] > hrana.getCena() + vzdialenosti[riadiaciVrchol]) {
                    vzdialenosti[hrana.getVrcholDo()] = hrana.getCena() + vzdialenosti[riadiaciVrchol];
                    indexyVrcholov[hrana.getVrcholDo()] = riadiaciVrchol;
                    
                    // ak množina epsilon neobsahuje vrchol Do, pridať
                    if (!epsilon.contains(hrana.getVrcholDo())) {
                        epsilon.add(hrana.getVrcholDo());
                    }
                }
            }
        }

        // výpis
        casovac = (System.nanoTime() - casovac);
        System.out.println("Graf: " + this.graf.getNazov());
        System.out.println("Vrcholov: " + this.graf.getPocetVrcholov());
        System.out.println("Hrán: " + this.graf.getPocetHran());
        System.out.println("Cesta z vrcholu " + vrcholZ + " -> " + vrcholDo);
        System.out.println("Použitý algoritmus: basic ArrayList");
        int vzdialenost = vzdialenosti[vrcholDo];
        int nextVrchol = vrcholDo;
        List<Integer> postupnostVrcholov = new ArrayList<Integer>();
        
        while (nextVrchol != -1) {
            postupnostVrcholov.add(0, nextVrchol);
            nextVrchol = indexyVrcholov[nextVrchol];
        }
        
        System.out.println("Vzdialenosť: " + vzdialenost);
        System.out.print("Cesta: ");
        postupnostVrcholov.forEach(vrchol -> System.out.print(vrchol + "->"));
        System.out.println();
        System.out.println("Čas výpočtu: " + casovac / 1_000_000_000D + "s");
        System.out.println();
    }

    /**
     * Implementácia algoritmu LabelSet s predspracovaním dátovej štruktúry a stále-zoradenou
     * množinou epsilon pre efektívne vyhľadávanie
     * 
     * @param vrcholZ - začiatočný vrchol
     * @param vrcholDo - konečný vrchol
     */
    public void najdiVzdialenost2(int vrcholZ, int vrcholDo) {
    	// inicializácia premenných
        int[] vzdialenosti = new int[this.graf.getPocetVrcholov()];
        int[] indexyVrcholov = new int[this.graf.getPocetVrcholov()];
        Map<Integer, List<Hrana>> mapaHran = new HashMap<Integer, List<Hrana>>(); // mapa hrán pre rýchlejší prístup k potrebným hranám
        int riadiaciVrchol = 0;
        // množina epsilon, ktorá sa udržuje usporiadaná podľa vrchola s najmenšou vzdialenosťou k počiatočnému bodu
        PriorityQueue<Integer> epsilon = new PriorityQueue<Integer>(new Comparator<Integer>() {

            @Override
            public int compare(Integer vrchol1, Integer vrchol2) {
                if (vzdialenosti[vrchol1] > vzdialenosti[vrchol2]) return 1;
                if (vzdialenosti[vrchol1] < vzdialenosti[vrchol2]) return -1;
                return 0;
            }
        });
        
        // inicializácia polí vzdialeností a indexov vrcholov
        for (int i = 0; i < vzdialenosti.length; i++) {
            vzdialenosti[i] = MAX_VALUE;
            indexyVrcholov[i] = -1;
        }
        
        vzdialenosti[vrcholZ] = 0;
        
        // vytvorenie mapy hrán
        for (Hrana hrana : this.graf.getZoznamHran()) {
            if (!mapaHran.containsKey(hrana.getVrcholZ())) {
                mapaHran.put(hrana.getVrcholZ(), new ArrayList<Hrana>());
            }
            
            mapaHran.get(hrana.getVrcholZ()).add(hrana);
        }
        
        // pridať vrchol (Set nedovoľuje duplicitné hodnoty - nič sa nestane)
        epsilon.add(vrcholZ);
        
        long casovac = System.nanoTime();
        
        while (!epsilon.isEmpty()) {
            riadiaciVrchol = epsilon.poll(); // získanie riadiaceho vrchola
            
            // pri správnom grafe by táto podmienka nikdy nemala byť pravdivá
            if (mapaHran.get(riadiaciVrchol) == null) {
                continue;
            }
            
            // prejdenie všetkých hrán vychádzajúcich z daného vrchola
            for (Hrana hrana : mapaHran.get(riadiaciVrchol)) {
                // aplikovanie podmienky
                if (vzdialenosti[hrana.getVrcholDo()] > hrana.getCena() + vzdialenosti[riadiaciVrchol]) {
                    vzdialenosti[hrana.getVrcholDo()] = hrana.getCena() + vzdialenosti[riadiaciVrchol];
                    indexyVrcholov[hrana.getVrcholDo()] = riadiaciVrchol;
                    epsilon.add(hrana.getVrcholDo());
                }
            }
        }
        
        // výpis
        casovac = (System.nanoTime() - casovac);
        System.out.println("Graf: " + this.graf.getNazov());
        System.out.println("Vrcholov: " + this.graf.getPocetVrcholov());
        System.out.println("Hrán: " + this.graf.getPocetHran());
        System.out.println("Cesta z vrcholu " + vrcholZ + " -> " + vrcholDo);
        System.out.println("Použitý algoritmus: optimalizovaný s PriorityQueue a HashMap");
        int vzdialenost = vzdialenosti[vrcholDo];
        int nextVrchol = vrcholDo;
        List<Integer> postupnostVrcholov = new ArrayList<Integer>();
        
        while (nextVrchol != -1) {
            postupnostVrcholov.add(0, nextVrchol);
            nextVrchol = indexyVrcholov[nextVrchol];
        }
        
        System.out.println("Vzdialenosť: " + vzdialenost);
        System.out.print("Cesta: ");
        postupnostVrcholov.forEach(vrchol -> System.out.print(vrchol + "->"));
        System.out.println();
        System.out.println("Čas výpočtu: " + casovac / 1_000_000_000D + "s");
        System.out.println();
    }
    
    public void najdiVzdialenost3(int zaciatocny, int konecny) {
        ArrayList<Integer> mnozinaEpsilon = new ArrayList<Integer>();
        HashMap<Integer, ArrayList<Hrana>> vrcholyDoHran = new HashMap<Integer, ArrayList<Hrana>>();
        int[] najlepsiaCena = new int[this.graf.getPocetVrcholov()];
        int[] posledneIndexy = new int[this.graf.getPocetVrcholov()];
        int riadiaci = zaciatocny;
        
        for (int i = 0; i < this.graf.getPocetVrcholov(); i++) {
            najlepsiaCena[i] = MAX_VALUE;
            posledneIndexy[i] = -1;
        }
        
        for (int j =0; j < this.graf.getZoznamHran().size(); j++ ) {
            Hrana dalsiaHrana = this.graf.getZoznamHran().get(j);
            
            if (!vrcholyDoHran.containsKey(dalsiaHrana.getVrcholZ())) {
                vrcholyDoHran.put(dalsiaHrana.getVrcholZ(), new ArrayList<Hrana>());
                vrcholyDoHran.get(dalsiaHrana.getVrcholZ()).add(dalsiaHrana);
            } else {
                vrcholyDoHran.get(dalsiaHrana.getVrcholZ()).add(dalsiaHrana);
            }
        }
        
        mnozinaEpsilon.add(zaciatocny);
        najlepsiaCena[zaciatocny] = 0;
        
        long casovac = System.nanoTime();
        
        while (mnozinaEpsilon.size() > 0) {
            int najnizsia = MAX_VALUE;
            
            for (int k = 0; k < mnozinaEpsilon.size(); k++) {
                if (najlepsiaCena[mnozinaEpsilon.get(k)] < najnizsia) {
                    najnizsia = najlepsiaCena[mnozinaEpsilon.get(k)];
                    riadiaci = mnozinaEpsilon.get(k);
                }
            }
            
            mnozinaEpsilon.remove((Integer)riadiaci);
            
            if (vrcholyDoHran.get(riadiaci) == null) {
                continue;
            }
            
            for (Hrana hranaZRiadiaceho : vrcholyDoHran.get(riadiaci)) {
                int novaNajmensiaCesta = hranaZRiadiaceho.getCena() + najlepsiaCena[riadiaci];
                
                if (najlepsiaCena[hranaZRiadiaceho.getVrcholDo()] > novaNajmensiaCesta) {
                    posledneIndexy[hranaZRiadiaceho.getVrcholDo()] = riadiaci;
                    najlepsiaCena[hranaZRiadiaceho.getVrcholDo()] = novaNajmensiaCesta;
                    
                    if (!mnozinaEpsilon.contains(hranaZRiadiaceho.getVrcholDo())) {
                        mnozinaEpsilon.add(hranaZRiadiaceho.getVrcholDo());
                    }
                }
            }
        }
        
        // výpis
        casovac = (System.nanoTime() - casovac);
        System.out.println("Graf: " + this.graf.getNazov());
        System.out.println("Vrcholov: " + this.graf.getPocetVrcholov());
        System.out.println("Hrán: " + this.graf.getPocetHran());
        System.out.println("Cesta z vrcholu " + zaciatocny + " -> " + konecny);
        System.out.println("Použitý algoritmus: optimalizovaný s HashMap");
        int vzdialenost = najlepsiaCena[konecny];
        ArrayList<Integer> cesta = new ArrayList<>();

        for (int i = konecny; i > -1; i = posledneIndexy[i]) {
            cesta.add(0, i);
        }
        
        System.out.println("Vzdialenosť: " + vzdialenost);
        System.out.print("Cesta: ");
        cesta.forEach(vrchol -> System.out.print(vrchol + "->"));
        System.out.println();
        System.out.println("Čas výpočtu: " + casovac / 1_000_000_000D + "s");
        System.out.println();
    }
}
