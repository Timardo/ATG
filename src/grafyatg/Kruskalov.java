package grafyatg;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Timardo
 *
 */
public class Kruskalov {
    
    private Graf graf;
    
    public Kruskalov(GrafEnum grafTyp) {
        this.graf = grafTyp.nacitajGraf();
    }
    
    /**
     * Vytvorí kostru a vypíše počet jej hrán a jej cenu
     * 
     * @param najvacsia - true ak chceme najväčšiu kostru, false ak najmenšiu
     */
    public void urobKostru(boolean najvacsia) {
        long casovac = System.nanoTime();
        int[] komponenty = new int[this.graf.getPocetVrcholov()]; // komponenty priradené k vrcholom
        // inicializácia komponentov
        for (int i = 0; i < komponenty.length; i++) {
            komponenty[i] = i;
        }
        
        List<Hrana> hranyKostry = new ArrayList<Hrana>(); //hrany tvoriace kostru
        List<Hrana> hrany = heapSort(this.graf.getZoznamHran(), new CompareFunction<Hrana>() { // zoradené hrany
            
            @Override
            public boolean isHigher(Hrana hrana1, Hrana hrana2) {
                return hrana1.getCena() > hrana2.getCena();
            }
        });
        
        hrany.remove(hrany.size() - 1); // odobratie nulovej hrany
        
        boolean jedenKomponent = false; // kontrola jedného komponentu, keď sa minú hrany aby nenastala chyba
        
        while (!jedenKomponent || !hrany.isEmpty()) {
            Hrana hrana = hrany.remove(najvacsia ? 0 : hrany.size() - 1); // získavanie hrany podľa vstupu
            // kontrola jedného komponentu
            if (komponenty[hrana.getVrcholZ()] == komponenty[hrana.getVrcholDo()]) {
                continue;
            }
            
            jedenKomponent = true;
            hranyKostry.add(hrana);
            int novyKomponent = komponenty[hrana.getVrcholZ()];
            int staryKomponent = komponenty[hrana.getVrcholDo()];
            // zjednotenie komponentov
            for (int i = 1; i < komponenty.length; i++) {
                if (komponenty[i] == staryKomponent) {
                    komponenty[i] = novyKomponent;
                } else if (jedenKomponent && novyKomponent != komponenty[i]) {
                    jedenKomponent = false;
                }
            }
        }
        // výpis
        if (!jedenKomponent) {
            System.out.println("Graf nie je súvislý");
            return;
            
        }
        
        long hodnotaKostry = 0;
        
        for (Hrana hrana : hranyKostry) {
            hodnotaKostry += hrana.getCena();
        }
        
        casovac = (System.nanoTime() - casovac);
        System.out.println("Graf: " + this.graf.getNazov());
        System.out.println("Typ kostry: " + (najvacsia ? "najväčšia" : "najmenšia"));
        System.out.println("Použitý algoritmus: basic objektový HeapSort");
        System.out.println("Počet hrán v kostre: " + hranyKostry.size());
        System.out.println("Cena kostry: " + hodnotaKostry);
        System.out.println("Čas výpočtu: " + casovac / 1_000_000_000D + "s");
        System.out.println();
    }
    
    /**
     * Vytvorí kostru a vypíše počet jej hrán a jej cenu optimalizovaným spôsobom
     * 
     * @param najvacsia - true ak chceme najväčšiu kostru, false ak najmenšiu
     */
    public void urobKostru2(boolean najvacsia) {
        int[] komponenty = new int[this.graf.getPocetVrcholov()]; // komponenty prislúchajúce vrcholom 
        int[][] mapaVrcholov = new int[this.graf.getPocetVrcholov()][]; // obsahuje všetky vrcholy prislúchajúce jednému komponentu
        int pocetHran = 0;
        long hodnotaKostry = 0;
        int pocetKomponentov = mapaVrcholov.length;
        // inicializácia
        for (int i = 0; i < komponenty.length; i++) {
            komponenty[i] = i;
            mapaVrcholov[i] = new int[] {i};
        }
        
        long casovac = System.nanoTime();
        // zoradenie hrán od najväčšej ceny po najmenšiu
        int[][] hrany = heapSortArray(this.graf.getHrany());
        int i = najvacsia ? 0 : hrany.length - 1; // podľa podmienky sa rozhodne či ísť od začiatku alebo od konca zoradených hrán
        
        while (i >= 0 && i < hrany.length) {
            if (!(komponenty[hrany[i][0]] == komponenty[hrany[i][1]])) { // preskočiť ak sa komponenty rovnajú
            	pocetHran++;
                hodnotaKostry += hrany[i][2];
                int novyKomponent = komponenty[hrany[i][0]];
                int staryKomponent = komponenty[hrany[i][1]];
                // prepísanie starých komponentov na nové
                for (int k : mapaVrcholov[staryKomponent]) {
                    komponenty[k] = novyKomponent;
                }
                // spojenie komponentov v mape
                int[] noveKomponenty = new int[mapaVrcholov[staryKomponent].length + mapaVrcholov[novyKomponent].length];
                System.arraycopy(mapaVrcholov[staryKomponent], 0, noveKomponenty, 0, mapaVrcholov[staryKomponent].length);
                System.arraycopy(mapaVrcholov[novyKomponent], 0, noveKomponenty, mapaVrcholov[staryKomponent].length, mapaVrcholov[novyKomponent].length);
                mapaVrcholov[novyKomponent] = noveKomponenty;
                mapaVrcholov[staryKomponent] = null;
                pocetKomponentov--;
            }
            // zmeniť počítadlo podľa podmienky
            i += najvacsia ? 1 : -1;
        }
        // výpis
        if (pocetKomponentov > 2) {
            System.out.println("Graf nie je súvislý");
            return;
        }
        
        casovac = (System.nanoTime() - casovac);
        System.out.println("Graf: " + this.graf.getNazov());
        System.out.println("Typ kostry: " + (najvacsia ? "najväčšia" : "najmenšia"));
        System.out.println("Použitý algoritmus: optimalizovaný");
        System.out.println("Počet hrán v kostre: " + pocetHran);
        System.out.println("Cena kostry: " + hodnotaKostry);
        System.out.println("Čas výpočtu: " + casovac / 1_000_000_000D + "s");
        System.out.println();
    }
    
    /**
     * Vlastná implementácia heap sortu skopírovaná pre zoradenie hrán
     * 
     * @param <T> - objektový typ, ktorý sa má zoraďovať
     * @param unsortedList - nezoradený list
     * @param comparator - comparator rozhodujúci o tom, ktorý prvok daného typu má väčšiu hodnotu
     * @return zoradený list
     */
    public static <T> List<T> heapSort(List<T> unsortedList, CompareFunction<T> comparator) {
        List<T> heap = new ArrayList<T>();

        int lastIndex = 0;

        for (T element : unsortedList) {
            heap.add(element);

            int tempIndex = lastIndex++;

            while (tempIndex != 0 && comparator.isHigher(element, heap.get((tempIndex - 1) / 2))) {
                heap.set(tempIndex, heap.get((tempIndex - 1) / 2));
                tempIndex = (tempIndex - 1) / 2;
                heap.set(tempIndex, element);
            }
        }

        List<T> sortedList = new ArrayList<T>();
        
        while (heap.size() > 0) {
            sortedList.add(removeRoot(heap, comparator));
        }

        return sortedList;
    }
    
    /**
     * Vlastná implementácia heap sortu skopírovaná pre zoradenie hrán a prispôsobená
     * dátovej štruktúre hrán vo forme arrayov
     * 
     * @param unsortedArray - nezoradený array hrán
     * @return zoradený array hrán
     */
    public static int[][] heapSortArray(int[][] unsortedArray) {
        int[][] heap = new int[unsortedArray.length][];

        int lastIndex = 0;
        int i = 0;

        for (int[] element : unsortedArray) {
            heap[i] = element;
            i++;
            
            int tempIndex = lastIndex++;

            while (tempIndex != 0 && element[2] > heap[(tempIndex - 1) / 2][2]) {
                heap[tempIndex] = heap[(tempIndex - 1) / 2];
                tempIndex = (tempIndex - 1) / 2;
                heap[tempIndex] = element;
            }
        }

        int[][] sortedArray = new int[unsortedArray.length][];
        
        for (i = 0; i < sortedArray.length; i++) {
            sortedArray[i] = removeRootArray(heap, i);
        }

        return sortedArray;
    }

    /**
     * Časť heap sortu, ktorá odstraňuje koreň a zanecháva heap zoradený
     * 
     * @param heap - heap ako array hrán
     * @param offset - offset pre počet odstránených koreňov
     * @return
     */
    private static int[] removeRootArray(int[][] heap, int offset) {
    	int[] ret = heap[0];
        int last = 0;
        heap[0] = heap[heap.length - 1 - offset];

        while (true) {
            if (last * 2 + 1 > heap.length - 1 - offset) break;

            int max = 0;

            if (last * 2 + 1 == heap.length - 1 - offset)
                max = 1;
            else
                max = Math.max(heap[last * 2 + 1][2], heap[last * 2 + 2][2]) == heap[last * 2 + 1][2] ? 1 : 2;

            if (!(heap[last * 2 + max][2] > heap[last][2])) break;

            int[] n = heap[last * 2 + max];
            heap[last * 2 + max] = heap[last];
            heap[last] = n;
            last = last * 2 + max;
        }

        return ret;
	}

	/**
     * Časť heap sortu, ktorá odstraňuje koreň a zanecháva heap zoradený
     * 
     * @param <T> - typ objektu
     * @param heap - heap ako list
     * @param comparator - comparator použitý pre rozhodovanie o veľkosti objektov
     * @return
     */
    private static <T> T removeRoot(List<T> heap, CompareFunction<T> comparator) {
        T ret = heap.get(0);
        int last = 0;
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);

        while (true) {
            if (last * 2 + 1 > heap.size() - 1) break;

            int max = 0;

            if (last * 2 + 1 == heap.size() - 1)
                max = 1;
            else
                max = max(heap.get(last * 2 + 1), heap.get(last * 2 + 2), comparator) == heap.get(last * 2 + 1) ? 1 : 2;

            if (!comparator.isHigher(heap.get(last * 2 + max), heap.get(last))) break;

            T n = heap.get(last * 2 + max);
            heap.set(last * 2 + max, heap.get(last));
            heap.set(last, n);
            last = last * 2 + max;
        }

        return ret;
    }
    
    /**
     * Rozhoduje o tom, ktorý (prvý alebo druhý) objekt má väčšiu hodnotu pri použití
     * dodaného comparatora
     * 
     * @param <T> - typ objektu
     * @param first - prvý objekt, testuje sa či je väčší
     * @param second - druhý objekt, testuje sa či je menší
     * @param comparator - comparator
     * @return
     */
    private static <T> T max(T first, T second, CompareFunction<T> comparator) {
        return comparator.isHigher(first, second) ? first : second;
    }
    
    public interface CompareFunction<T> {
        /**
         * Implementácia testuje či prvý parameter je väčší ako druhý
         * 
         * @param value1
         * @param value2
         * @return
         */
        public boolean isHigher(T value1, T value2);
    }
}
