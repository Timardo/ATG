package grafyatg;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Timardo
 *
 */
public class Tarryho {
    
    private Graf graf;
    
    public Tarryho(GrafEnum grafTyp) {
        this.graf = grafTyp.nacitajGraf();
    }
    
    public void urobSled(int zaciatok) {
        HashMap<Integer, ArrayList<Hrana>> mapaHran = new HashMap<Integer, ArrayList<Hrana>>();
        boolean[] prejdene = new boolean[this.graf.getPocetVrcholov()];
        int riadiaciVrchol = zaciatok;
        ArrayList<Integer> sled = new ArrayList<Integer>();
        sled.add(riadiaciVrchol);
        
        for (Hrana hrana : this.graf.getZoznamHran()) {
            if (!mapaHran.containsKey(hrana.getVrcholZ())) {
                mapaHran.put(hrana.getVrcholZ(), new ArrayList<Hrana>());
            }
            
            mapaHran.get(hrana.getVrcholZ()).add(hrana);
        }
        
        long casovac = System.nanoTime();
        
        while (true) {
            Hrana dalsia = null;
            
            for (Hrana hrana : mapaHran.get(riadiaciVrchol)) {
                Hrana opacna = getOpacnaHrana(hrana, mapaHran);
                
                if (opacna.isPrejdena() || opacna.isPrvyPrichod() || hrana.isPrejdena()) continue;
                dalsia = hrana;
            }
            
            if (dalsia == null) {
                for (Hrana hrana : mapaHran.get(riadiaciVrchol)) {
                    Hrana opacna = getOpacnaHrana(hrana, mapaHran);
                    
                    if (opacna.isPrvyPrichod() || hrana.isPrejdena()) continue;
                    dalsia = hrana;
                }
            }
            
            if (dalsia == null) {
                for (Hrana hrana : mapaHran.get(riadiaciVrchol)) {
                    
                    if (hrana.isPrejdena()) continue;
                    dalsia = hrana;
                }
            }
            
            if (dalsia == null) {
                break;
            }
            
            dalsia.setPrejdena(true);
            
            if (!prejdene[dalsia.getVrcholDo()]) {
                prejdene[dalsia.getVrcholDo()] = true;
                dalsia.setPrvyPrichod(true);
            }
            
            riadiaciVrchol = dalsia.getVrcholDo();
            sled.add(riadiaciVrchol);
        }
        
        casovac = (System.nanoTime() - casovac);
        System.out.println("Graf: " + this.graf.getNazov());
        System.out.println("Tarryho sled: ");
        sled.forEach(element -> System.out.print(element + "-"));
        System.out.println();
        System.out.println("Čas výpočtu: " + casovac / 1_000_000_000D + "s");
        System.out.println();
    }

    private Hrana getOpacnaHrana(Hrana hrana, HashMap<Integer, ArrayList<Hrana>> mapaHran) {
        for (Hrana opacna : mapaHran.get(hrana.getVrcholDo())) {
            if (opacna.getVrcholDo() == hrana.getVrcholZ()) return opacna;
        }

        return null; // TOTO BY SA NEMALO STAŤ
    }
}
