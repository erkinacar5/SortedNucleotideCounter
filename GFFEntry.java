package mappednucleotidecount;
/**
 *
 * @author erkin
 */
public class GFFEntry {
    public int START, END; //Start and end position of the gff entry
    public String NAME, CHR; //The ID of the entry and on which chromosome it is.
    
    public void getGFFEntry(String s){
        String[] columns = s.split("\t");
        START = Integer.valueOf(columns[3]);
        END = Integer.valueOf(columns[4]);
        CHR = columns[0];
        NAME = columns[8];
    }
}
