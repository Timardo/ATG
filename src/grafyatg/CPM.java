package grafyatg;

import java.util.ArrayList;

/**
 * 
 * @author Timardo
 *
 */
public class CPM {
    private Graf graf;

    public CPM(GrafEnum grafTyp) {
        this.graf = grafTyp.nacitajGraf();
    }
    
    public void urobCPM() {
        this.graf.addHranyToVrcholDiGraph();
        long casovac = System.nanoTime();
        ArrayList<Vrchol> monotonneOcislovanie = new MonotonneOcislovanie(this.graf).ocisluj();
        // +1 - monotónne očíslovanie neobsahuje nulový vrchol
        int[] zaciatky = new int[monotonneOcislovanie.size() + 1];
        int[] konce = new int[monotonneOcislovanie.size() + 1];
        int[] maxTrvanie = new int[monotonneOcislovanie.size() + 1];
        int[] rezervy = new int[monotonneOcislovanie.size() + 1];
        int trvanieT = 0;
        
        for (int i = 0; i < monotonneOcislovanie.size(); i++) {
            Vrchol v = monotonneOcislovanie.get(i);
            
            for (Hrana h : v.getVystupneHrany()) {
                if (zaciatky[h.getVrcholDo()] < zaciatky[h.getVrcholZ()] + v.getTrvanie()) {
                    zaciatky[h.getVrcholDo()] = zaciatky[h.getVrcholZ()] + v.getTrvanie();
                }
            }
        }
        
        // od 1 - ignorujem nulový vrchol
        for (int i = 1; i < konce.length; i++) {
            trvanieT = Math.max(trvanieT, zaciatky[i] + this.graf.getZoznamVrcholov().get(i).getTrvanie());
        }
        
        for (int i = 0; i < konce.length; i++) {
            konce[i] = trvanieT;
        }
        
        for (int i = monotonneOcislovanie.size() - 1; i >= 0; i--) {
            Vrchol v = monotonneOcislovanie.get(i);
            
            for (Hrana h : v.getVstupneHrany()) {
                if (konce[h.getVrcholZ()] > konce[h.getVrcholDo()] - v.getTrvanie()) {
                    konce[h.getVrcholZ()] = konce[h.getVrcholDo()] - v.getTrvanie();
                }
            }
        }
        
        ArrayList<Integer> kritickeVrcholy = new ArrayList<Integer>();
        
        for (int i = 1; i < maxTrvanie.length; i++) {
            maxTrvanie[i] = konce[i] - zaciatky[i];
            rezervy[i] = maxTrvanie[i] - this.graf.getZoznamVrcholov().get(i).getTrvanie();
            
            if (rezervy[i] == 0) {
                kritickeVrcholy.add(i);
            }
        }
        
        casovac = (System.nanoTime() - casovac);
        System.out.println("Graf: " + this.graf.getNazov());
        System.out.println("Trvanie projektu je: " + trvanieT);
        System.out.print("Kritické vrcholy sú: ");
        kritickeVrcholy.forEach(vrchol -> System.out.print(vrchol + ", "));
        System.out.println();
        
        
        System.out.print("Najskôr možné začiatky: [");
        
        for (int i = 0; i < zaciatky.length - 1; i++) {
            System.out.print(zaciatky[i] + ", ");
        }
        
        System.out.println(zaciatky[zaciatky.length - 1] + "]");
        
        System.out.print("Najneskôr možné konce: [");
        
        for (int i = 0; i < konce.length - 1; i++) {
            System.out.print(konce[i] + ", ");
        }
        
        System.out.println(konce[konce.length - 1] + "]");
        System.out.println("Čas výpočtu: " + casovac / 1_000_000_000D + "s");
        System.out.println();
    }
}
