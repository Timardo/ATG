package grafyatg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * 
 * @author Timardo
 *
 */
public class MaximalnyTok {
    
    private Graf graf;
    private int[] predchodcovia;
    private HashMap<Integer, HashMap<Integer, Hrana>> mapaHranVrcholov;
    
    public MaximalnyTok(Graf g) {
        this.graf = g;
        this.predchodcovia = new int[this.graf.getPocetVrcholov()];
        this.mapaHranVrcholov = new HashMap<Integer, HashMap<Integer,Hrana>>();

    }
    
    public void urobNajlacnejsiMaximalnyTok() {
        // maximálny tok začiatok
        int zdroj = 1;
        int uzaver = this.graf.getPocetVrcholov() - 1;
        int maximálnyTok = 0;
        this.graf.addHranyToVrcholDiGraph();
        
        for (Vrchol vrchol : this.graf.getZoznamVrcholov()) {
            for (Hrana hrana : vrchol.getVystupneHrany()) {
                if (this.mapaHranVrcholov.get(hrana.getVrcholZ()) == null) {
                    this.mapaHranVrcholov.put(hrana.getVrcholZ(), new HashMap<Integer, Hrana>());
                }
                
                this.mapaHranVrcholov.get(hrana.getVrcholZ()).put(hrana.getVrcholDo(), hrana);
            }
        }
        
        boolean maPolocestu = this.najdiPolocestu(zdroj, uzaver);
        
        while (maPolocestu) {
            int vrchol = uzaver;
            int rezervaPolocesty = this.predchodcovia[0];
            
            while (true) {
                int druhyVrchol = this.predchodcovia[vrchol];
                
                if (druhyVrchol < 0) {
                    Hrana hrana = this.mapaHranVrcholov.get(vrchol).get(-druhyVrchol);
                    hrana.setTok(hrana.getTok() - rezervaPolocesty);
                    vrchol = -druhyVrchol;
                } else if (druhyVrchol > 0) {
                    Hrana hrana = this.mapaHranVrcholov.get(druhyVrchol).get(vrchol);
                    hrana.setTok(hrana.getTok() + rezervaPolocesty);
                    vrchol = druhyVrchol;
                } else break;
            }
            
            maPolocestu = this.najdiPolocestu(zdroj, uzaver);
        }
        
        for (Hrana hrana : this.graf.getZoznamVrcholov().get(zdroj).getVystupneHrany()) {
            maximálnyTok += hrana.getTok();
        }
        
        // maximálny tok koniec, najlacnejší maximálny tok začiatok
        int[] rezervaAPrvok = this.najdiRezervnyPolocyklus();
        
        while (rezervaAPrvok != null) {
            int rezerva = rezervaAPrvok[1];
            int vrchol = rezervaAPrvok[0];
            
            while (true) {
                int druhyVrchol = this.predchodcovia[vrchol];
                
                if (druhyVrchol < 0) {
                    Hrana hrana = this.mapaHranVrcholov.get(vrchol).get(-druhyVrchol);
                    hrana.setTok(hrana.getTok() - rezerva);
                } else if (druhyVrchol > 0) {
                    Hrana hrana = this.mapaHranVrcholov.get(druhyVrchol).get(vrchol);
                    hrana.setTok(hrana.getTok() + rezerva);
                }
                
                vrchol = Math.abs(druhyVrchol);
                
                if (vrchol == rezervaAPrvok[0]) break;
            }
            
            rezervaAPrvok = this.najdiRezervnyPolocyklus();
        }
        
        int cenaToku = 0;
        
        for (Hrana hrana : this.graf.getZoznamHran()) {
            cenaToku += hrana.getCena() * hrana.getTok();
        }
        
        System.out.println("Maximálny tok: " + maximálnyTok);
        System.out.println("Minimálna cena maximálneho toku: " + cenaToku);
    }

    private int[] najdiRezervnyPolocyklus() {
        int[] ceny = new int[this.graf.getPocetVrcholov()];
        LinkedHashSet<Vrchol> epsilon = new LinkedHashSet<Vrchol>();
        epsilon.addAll(this.graf.getZoznamVrcholov());
        
        for (int i = 0; i < ceny.length; i++) {
            ceny[i] = 0;
            this.predchodcovia[i] = 0;
        }
        
        while (!epsilon.isEmpty()) {
            Vrchol v = epsilon.iterator().next();
            epsilon.remove(v);
            
            for (Hrana vystupna : v.getVystupneHrany()) {
                if (ceny[vystupna.getVrcholDo()] > ceny[v.getNazov()] + vystupna.getCena() && vystupna.getTok() < vystupna.getKapacita()) {
                    ceny[vystupna.getVrcholDo()] = ceny[v.getNazov()] + vystupna.getCena();
                    this.predchodcovia[vystupna.getVrcholDo()] = v.getNazov();
                    epsilon.add(this.graf.getZoznamVrcholov().get(vystupna.getVrcholDo()));
                }
            }
            
            for (Hrana vstupna : v.getVstupneHrany()) {
                if (ceny[vstupna.getVrcholZ()] > ceny[v.getNazov()] - vstupna.getCena() && vstupna.getTok() > 0) {
                    ceny[vstupna.getVrcholZ()] = ceny[v.getNazov()] - vstupna.getCena();
                    this.predchodcovia[vstupna.getVrcholZ()] = -v.getNazov();
                    epsilon.add(this.graf.getZoznamVrcholov().get(vstupna.getVrcholZ()));
                }
            }
            
            int rezerva = this.maRezervu(v.getNazov());
            
            if (rezerva != -1) {
                return new int[] {v.getNazov(), rezerva};
            }
        }
        
        return null;
    }

    private int maRezervu(int vrchol) {
        int rezerva = Integer.MAX_VALUE;
        int riadiaci = vrchol;
        
        while (true) {
            int dalsiVrchol = this.predchodcovia[riadiaci];
            
            if (dalsiVrchol < 0) {
                Hrana hrana = this.mapaHranVrcholov.get(riadiaci).get(-dalsiVrchol);
                
                if (rezerva > hrana.getTok()) {
                    rezerva = hrana.getTok();
                }
            } else if (dalsiVrchol > 0) {
                Hrana hrana = this.mapaHranVrcholov.get(dalsiVrchol).get(riadiaci);
                
                if (rezerva > hrana.getKapacita() - hrana.getTok()) {
                    rezerva = hrana.getKapacita() - hrana.getTok();                
                }
            } else {
                return -1;
            }
            
            riadiaci = Math.abs(dalsiVrchol);
            
            if (riadiaci == vrchol) return rezerva;
        }
    }

    private boolean najdiPolocestu(int zdroj, int uzaver) {
        ArrayList<Integer> epsilon = new ArrayList<Integer>();
        int rezervaPolocesty = Integer.MAX_VALUE;
        epsilon.add(zdroj);
        
        for (int i = 2; i < this.predchodcovia.length; i++) {
            this.predchodcovia[i] = Integer.MAX_VALUE;
        }
        
        while (this.predchodcovia[uzaver] == Integer.MAX_VALUE && !epsilon.isEmpty()) {
            int riadiaci = epsilon.remove(0);
            
            for (Hrana vystupna : this.graf.getZoznamVrcholov().get(riadiaci).getVystupneHrany()) {
                int rezerva = vystupna.getKapacita() - vystupna.getTok();
                
                if (this.predchodcovia[vystupna.getVrcholDo()] == Integer.MAX_VALUE && rezerva > 0) {
                    this.predchodcovia[vystupna.getVrcholDo()] = riadiaci;
                    epsilon.add(vystupna.getVrcholDo());
                    
                    if (rezervaPolocesty > rezerva) {
                        rezervaPolocesty = rezerva;
                    }
                }
            }
            
            for (Hrana vstupna : this.graf.getZoznamVrcholov().get(riadiaci).getVstupneHrany()) {
                if (this.predchodcovia[vstupna.getVrcholZ()] == Integer.MAX_VALUE && vstupna.getTok() > 0) {
                    this.predchodcovia[vstupna.getVrcholZ()] = -riadiaci;
                    epsilon.add(vstupna.getVrcholZ());
                    
                    if (rezervaPolocesty > vstupna.getTok()) {
                        rezervaPolocesty = vstupna.getTok();
                    }
                }
            }
        }
        
        this.predchodcovia[0] = rezervaPolocesty; // rezerva polocesty je uložená na nulovom indexe predchodcov
        return this.predchodcovia[uzaver] < Integer.MAX_VALUE;
    }
}
