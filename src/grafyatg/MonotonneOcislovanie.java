package grafyatg;

import java.util.ArrayList;

/**
 * 
 * @author Timardo
 *
 */
public class MonotonneOcislovanie {
    
    private Graf graf;
    
    public MonotonneOcislovanie(Graf g) {
        this.graf = g;
    }
    
    public ArrayList<Vrchol> ocisluj() {
        ArrayList<Vrchol> ocislovanie = new ArrayList<Vrchol>();
        int[] vstupneStupne = new int[this.graf.getPocetVrcholov()];
        
        for (int i = 0; i < vstupneStupne.length; i++) {
            vstupneStupne[i] = this.graf.getZoznamVrcholov().get(i).getVstupneHrany().size();
        }
        
        while (true) {
            Vrchol riadiaci = null;
            
            for (int i = 0; i < vstupneStupne.length; i++) {
                if (vstupneStupne[i] == 0) {
                    vstupneStupne[i] = -1;
                    riadiaci = this.graf.getZoznamVrcholov().get(i);
                    break;
                }
            }
            
            if (riadiaci == null) {
                break;
            }
            
            ocislovanie.add(riadiaci);
            
            for (Hrana hrana : riadiaci.getVystupneHrany()) {
                vstupneStupne[hrana.getVrcholDo()]--;
            }
        }
        
        if (ocislovanie.size() == this.graf.getPocetVrcholov() - 1) {
            System.out.print("Graf je acyklický, tu je jeho očíslovanie: ");
            ocislovanie.forEach(vrchol -> System.out.print(vrchol.getNazov() + "->"));
            System.out.println();
        } else {
            System.out.println("Graf nie je acyklický");
        }
        
        return ocislovanie;
    }
}
