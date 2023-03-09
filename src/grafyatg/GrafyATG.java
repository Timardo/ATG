package grafyatg;

/**
 * 
 * @author Timardo
 *
 */
public class GrafyATG {

    public static void main(String[] args) {
        LabelSet labelSet = new LabelSet(GrafEnum.SLOVAKIA);
        //labelSet.najdiVzdialenost(1, 325); // neodporúčam skúšať na slovensku a floride z časových dôvodov
        labelSet.najdiVzdialenost2(1, 325);
        labelSet.najdiVzdialenost3(1, 325);
        
        //CPM cpm = new CPM(GrafEnum.CPM_MIDI);
        //cpm.urobCPM();
        
        //Tarryho tarryho = new Tarryho(GrafEnum.STRAKONICE); // nefunguje na väčších grafoch z nejakého dôvodu
        //tarryho.urobSled(1);
        
        //Kruskalov kruskalov = new Kruskalov(GrafEnum.FLORIDA);
        //kruskalov.urobKostru2(false);
        //kruskalov.urobKostru2(true);
        //kruskalov.urobKostru(false);
        //kruskalov.urobKostru(true);
    }

    /*private static void testTok() {
        String dir = "." + File.separator + "ATG_DAT" + File.separator + "Toky" + File.separator;
        String file = dir + "Tok_mini2.hrn";
        File f = new File(file);
        System.out.println(f.getAbsolutePath());
        Graf g = Graf.nacitajGrafToky(file);
        System.out.println("Vrcholov:" + g.getPocetVrcholov());
        System.out.println("Hrán:" + g.getPocetHran());
        
        MaximalnyTok maxTok = new MaximalnyTok(g);
        maxTok.urobNajlacnejsiMaximalnyTok();
    }
    
    private static void testTok2() {
        String dir = "." + File.separator + "ATG_DAT" + File.separator + "Toky" + File.separator;
        String file = dir + "Tok_mini.hrn";
        File f = new File(file);
        System.out.println(f.getAbsolutePath());
        Graf g = Graf.nacitajGrafToky(file);
        System.out.println("Vrcholov:" + g.getPocetVrcholov());
        System.out.println("Hrán:" + g.getPocetHran());
        
        Toky maxTok = new Toky(g);
        maxTok.maxTok();
    }*/
}
