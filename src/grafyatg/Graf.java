package grafyatg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Maroš
 */
public class Graf {
    
    private String nazovGrafu;
    private ArrayList<Vrchol> zoznamVrcholov; // smerníky na prvu hranu pre dany vrchol pomocou arraylistu
    private ArrayList<Hrana> zoznamHran; // zoznam hran (arraylist) pomocou objektu
    private int[][] hrany; // zoznam hran pomocou matice H[i][0] je zaciatocny vrchol, H[i][1] je koncovy vrchol a H[i][3] je cena hrany
    private int[] smerniky; // smerniky na prvu hranu pre dany vrchol
    private int pocetVrcholov;
    private int pocetHran;

    public static Graf nacitajGraf(String filename) {
        Input input = new Input();
        input.readData(filename);
        Graf g = new Graf(input.getZoznamVrcholov(), input.getZoznamHran(), input.getH(), input.getS(), input.getPocetVrcholov(), input.getPocetHran());
        return g;
    }
    
    public static Graf nacitajGrafToky(String filename) {
        Input input = new Input();
        input.readDataToky(filename);
        Graf g = new Graf(input.getZoznamVrcholov(), input.getZoznamHran(), input.getH(), input.getS(), input.getPocetVrcholov(), input.getPocetHran());
        return g;
    }

    public Graf(ArrayList<Vrchol> zoznamVrcholov, ArrayList<Hrana> zoznamHran, int[][] hrany, int[] smerniky, int pocetVrchlov, int pocetHran) {
        this.zoznamVrcholov = zoznamVrcholov;
        this.zoznamHran = zoznamHran;
        this.hrany = hrany;
        this.smerniky = smerniky;
        this.pocetVrcholov = pocetVrchlov;
        this.pocetHran = pocetHran;
    }

    public Graf(int[][] hrany, int[] smerniky, int pocetVrchlov, int pocetHran) {
        this.hrany = hrany;
        this.smerniky = smerniky;
        this.pocetVrcholov = pocetVrchlov;
        this.pocetHran = pocetHran;
    }
    
    public void setNazov(String novyNazov) {
        this.nazovGrafu = novyNazov;
    }
    
    public String getNazov() {
        return this.nazovGrafu;
    }

    public ArrayList<Vrchol> getZoznamVrcholov() {
        return this.zoznamVrcholov;
    }

    public ArrayList<Hrana> getZoznamHran() {
        return this.zoznamHran;
    }

    public int[][] getHrany() {
        return this.hrany;
    }

    public int[] getSmerniky() {
        return this.smerniky;
    }

    public int getPocetVrcholov() {
        return this.pocetVrcholov;
    }

    public int getPocetHran() {
        return this.pocetHran;
    }
    
    public void addHranyToVrcholDiGraph() {
        for (Hrana hrana : this.zoznamHran) {
            this.zoznamVrcholov.get(hrana.getVrcholZ()).pridajVystupnuHranu(hrana);
            this.zoznamVrcholov.get(hrana.getVrcholDo()).pridajVstupnuHranu(hrana);
        }
    }
    
    public int[] nacitajTrvaniaCinnosti(String name) {
        int[] trvania = new int[pocetVrcholov];
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(name)));
            String line;
            
            for (int i = 1; i < this.pocetVrcholov ; i++) {
                line = br.readLine();
                int trv = Integer.parseInt(line);
                trvania[i] = trv;
                this.zoznamVrcholov.get(i).setTrvanie(trv);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            // System.err.println("Subor neexistuje"); ignore keďže nie vždy sa načítavajú
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
        return trvania;
    }

}
