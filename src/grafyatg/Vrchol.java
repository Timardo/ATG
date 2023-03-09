package grafyatg;

import java.util.ArrayList;

/**
 * 1. 4. 2021 - 16:39
 *
 * @author Kristian
 */
public class Vrchol {
    private int nazov;
    private ArrayList<Hrana> vstupneHrany;
    private ArrayList<Hrana> vystupneHrany;
    private int cena;
    private int t;
    private int x;
    private int trvanie;

    public Vrchol(int cislo) {
        this.nazov = cislo;
        vstupneHrany = new ArrayList<>();
        vystupneHrany = new ArrayList<>();
    }

    public int getNazov() {
        return this.nazov;
    }

    public ArrayList<Hrana> getVstupneHrany() {
        return this.vstupneHrany;
    }

    public ArrayList<Hrana> getVystupneHrany() {
        return this.vystupneHrany;
    }
    public void pridajVystupnuHranu(Hrana h) {
        vystupneHrany.add(h);
    }
    public void pridajVstupnuHranu(Hrana h) {
        vstupneHrany.add(h);
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getCena() {
        return this.cena;
    }

    public int getTrvanie() {
        return this.trvanie;
    }

    public void setTrvanie(int trvanie) {
        this.trvanie = trvanie;
    }
}
