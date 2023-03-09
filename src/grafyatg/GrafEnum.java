package grafyatg;

import java.io.File;

/**
 * 
 * @author Timardo
 *
 */
public enum GrafEnum {
    FLORIDA("ShortestPath", "Florida.hrn"),
    NEW_YORK("ShortestPath", "NewYork.hrn"),
    SLOVAKIA("ShortestPath", "SlovRep.hrn"),
    STRAKONICE("ShortestPath", "Strakonice.hrn"),
    AC_DIGR("ACYKL", "AcDigr.hrn"),
    CPM_MINI("ACYKL", "CPM_mini.hrn"),
    CPM_STRED("ACYKL", "CPM_stred.hrn"),
    CPM_MIDI("ACYKL", "CPM_midi.hrn"),
    CYKL_MINI("CYKL_DIGRAF", "CYKL_mini.hrn"),
    CYKL_MAXI("CYKL_DIGRAF", "CYKL_maxi.hrn"),
    TOK_MINI("Toky", "Tok_mini.hrn"),
    TOK_MINI2("Toky", "Tok_mini2.hrn"),
    TOK_MIDI("Toky", "Tok_midi.hrn");
    
    String folder;
    String fileName;
    
    GrafEnum(String folder, String fileName) {
        this.folder = folder;
        this.fileName = fileName;
    }
    
    public Graf nacitajGraf() {
        String dir = "." + File.separator + "ATG_DAT" + File.separator + this.folder + File.separator;
        String file = dir + this.fileName;
        Graf g = Graf.nacitajGraf(file);
        g.nacitajTrvaniaCinnosti(file.replace("hrn", "tim"));
        g.setNazov(this.toString());
        return g;
    }
}
