package grafyatg;

import java.util.ArrayList;

/**
 * 
 * @author Timardo
 *
 */
public class Toky {
    private Graf graf;
    private int[] predVrcholy;
    private int uzaver;


    public Toky(Graf g) {
        this.graf = g;
        this.graf.addHranyToVrcholDiGraph();
        this.predVrcholy = new int[this.graf.getPocetVrcholov()];
        this.uzaver = graf.getPocetVrcholov() - 1;
    }

    public boolean najdiZvacsujucuSaPolocestu(){
        ArrayList<Integer> epsilon = new ArrayList<Integer>();
        epsilon.add(1); //začiatočný bod, zdroj
        for(int i = 0; i < predVrcholy.length; i++){
            predVrcholy[i] = Integer.MAX_VALUE;
        }
        predVrcholy[1] = 0;
        while (predVrcholy[uzaver] == Integer.MAX_VALUE && !epsilon.isEmpty()){
            int vrchol = epsilon.remove(0);
            for(Hrana hrana : this.graf.getZoznamVrcholov().get(vrchol).getVstupneHrany()){  //Vyberám všetky vstupne hrany z vrcholov
                if(predVrcholy[hrana.getVrcholZ()] == Integer.MAX_VALUE && hrana.getTok() > 0){ //rezerva musí byť väčšia ako nula
                    predVrcholy[hrana.getVrcholZ()] = -vrchol; //predchodcu nastavím na vrchol
                    epsilon.add(hrana.getVrcholZ());
                }
            }
            for(Hrana hrana : this.graf.getZoznamVrcholov().get(vrchol).getVystupneHrany()){  //Vyberám všetky vstupne hrany z vrcholov
                if(predVrcholy[hrana.getVrcholDo()] == Integer.MAX_VALUE && hrana.getKapacita() - hrana.getTok() > 0){ //rezerva tá pravá časť, výpočet rezervy (hrana.getKapacita() - hrana.getTok())
                    predVrcholy[hrana.getVrcholDo()] = vrchol; //predchodcu nastavím na vrchol
                    epsilon.add(hrana.getVrcholDo());
                }
            }
        }
        if (predVrcholy[uzaver] == Integer.MAX_VALUE) { //vypíšeme tok
            return false;
        }
        else {
            return true;
        }
    }
    
    public void maxTok() {
        while (najdiZvacsujucuSaPolocestu()) {
            int rezerva = Integer.MAX_VALUE;
            int konVrchol = predVrcholy[uzaver];
            for (int zacVrchol = uzaver;; zacVrchol = Math.abs(konVrchol)) { //postupne prechdáza hrany polocesty
                konVrchol = predVrcholy[zacVrchol];
                if (konVrchol == 0) break;
                if(konVrchol > 0){
                    Hrana hrana = najdiHranu(konVrchol, zacVrchol); //Najdeme hranu
                    if(rezerva > hrana.getKapacita() - hrana.getTok()){ //vyskusame rezervu a zapiseme
                        rezerva = hrana.getKapacita() - hrana.getTok();
                    }
                }
                else{
                    Hrana hrana = najdiHranu(zacVrchol, -konVrchol); //Najdeme rezervu ked sa dostanem na koncovu 0
                    if(rezerva > hrana.getTok()){ //
                        rezerva = hrana.getTok();
                    }
                }
            }
            konVrchol = predVrcholy[uzaver];
            for (int zacVrchol = uzaver;; zacVrchol = Math.abs(konVrchol)) { //postupne prechdáza hrany polocesty
                konVrchol = predVrcholy[zacVrchol];
                if (konVrchol == 0) break;
                if(konVrchol > 0){
                    Hrana hrana = najdiHranu(konVrchol, zacVrchol); //Najdeme hranu
                    hrana.setTok(hrana.getTok() + rezerva);
                }
                else{
                    Hrana hrana = najdiHranu(zacVrchol, -konVrchol); //Najdeme rezervu ked sa dostanem na koncovu 0
                    hrana.setTok(hrana.getTok() - rezerva);
                }
            }
        }
        
        int tok = 0;
        for(Hrana hrana : this.graf.getZoznamVrcholov().get(1).getVystupneHrany()){  //Vyberám všetky vstupne hrany z vrcholov
            tok += hrana.getTok();
        }
        System.out.println("Tok : " + tok);
    }

    public Hrana najdiHranu(int vrcholZ, int vrcholDo){
        for(Hrana hrana : this.graf.getZoznamVrcholov().get(vrcholZ).getVystupneHrany()){  //Vyberám všetky vstupne hrany z vrcholov
            if(hrana.getVrcholDo() == vrcholDo){ //rezerva tá pravá časť, výpočet rezervy (hrana.getKapacita() - hrana.getTok())
                return hrana;
            }
        }
        return null;
    }


}