package mappednucleotidecount;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author erkin
 */
public class MappedNucleotideCount {

    static File SAM, GFF, output;
    
    public static void main(String[] args) {
        parseArgs(args);
        try {
            BufferedReader br = new BufferedReader(new FileReader(GFF));
            ArrayList<GFFEntry> gff_list = new ArrayList<>();
            String line;
            int g = 0;
            String mappedChr, seq;
            int mapStart, seqLength, mapEnd;
            int j = 0;
            while ((line = br.readLine()) != null) {
                GFFEntry gff = new GFFEntry();
                gff.getGFFEntry(line);
                gff_list.add(j, gff);
                g++;
                j++;
            }
            br.close();
            Scanner s = new Scanner(SAM);
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));
            bw.write("Name\tCount\tNuc.Count");
            bw.newLine();
            int i = 0, count = 0, check = 0, mapCheck = 0;
            while (s.hasNext()) {
                if (check == 0) {
                    line = s.nextLine();
                }
                count = 0;
                String[] cols = line.split("\t");
                mappedChr = cols[2];
                seq = cols[9];
                seqLength = seq.length();
                mapStart = Integer.valueOf(cols[3]);
                mapEnd = mapStart + seqLength - 1;
                String geName = gff_list.get(i).NAME;
                String geChr = gff_list.get(i).CHR;
                double getStart = (double)(gff_list.get(i).START - Math.ceil((seqLength/4)));
                int geStart = (int) getStart;
                double getEnd = (double) (gff_list.get(i).END + Math.ceil(seqLength/4));
                int geEnd = (int) getEnd;
                if (mapCheck == 0) {
                    while (!geChr.trim().equals(mappedChr.trim())) {
                        if (i + 1 < gff_list.size()) {
                            i++;
                            geName = gff_list.get(i).NAME;
                            geChr = gff_list.get(i).CHR;
                            geStart = gff_list.get(i).START;
                            geEnd = gff_list.get(i).END;
                        } else {
                            break;
                        }
                    }
                    if (!geChr.trim().equals(mappedChr.trim())) {
                        i = 0;
                        geName = gff_list.get(i).NAME;
                        geChr = gff_list.get(i).CHR;
                        geStart = gff_list.get(i).START;
                        geEnd = gff_list.get(i).END;
                        while (!geChr.trim().equals(mappedChr.trim())) {
                            if (s.hasNext()) {
                                line = s.nextLine();
                                cols = line.split("\t");
                                mappedChr = cols[2];
                                seq = cols[9];
                                seqLength = seq.length();
                            }
                        }
                        check = 1;
                    }
                    mapCheck = 1;
                }
                while (!geChr.trim().equals(mappedChr.trim())) {
                    bw.write(geName + "\t" + 0 + "\t" + 0);
                    bw.newLine();
                    if (i + 1 < gff_list.size()) {
                        i++;
                        geName = gff_list.get(i).NAME;
                        geChr = gff_list.get(i).CHR;
                        geStart = gff_list.get(i).START;
                        geEnd = gff_list.get(i).END;
                    } else {
                        break;
                    }
                }
                while (geChr.trim().equals(mappedChr.trim())) {
                    if ((geStart - 5) < mapStart && (geEnd + 5) < mapEnd && (geEnd + 5) > mapStart) {
                        if (s.hasNext()) {
                            line = s.nextLine();
                            check = 1;
                            break;
                        } else {
                            break;
                        }
                    } else if ((geStart - 5) < mapStart && (geEnd + 5) < mapEnd) {
                        if (i + 1 < gff_list.size()) {
                            if (geChr.trim().equals(gff_list.get(i + 1).CHR)) {
                                i++;
                            } else if (s.hasNext()) {
                                line = s.nextLine();
                                check = 1;
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                        check = 1;
                        break;

                    } else if ((geStart - 5) <= mapStart && (geEnd + 5) >= mapEnd) {
                        count++;
                        bw.write(geName + "\t" + count + "\t" + seqLength);
                        bw.newLine();
                        if (s.hasNext()) {
                            line = s.nextLine();
                        } else {
                            break;
                        }
                        check = 1;
                        break;

                    } else if ((geStart - 5) > mapStart && (geEnd + 5) >= mapEnd) {
                        if (s.hasNext()) {
                            line = s.nextLine();
                            check = 1;
                            break;
                        } else {
                            break;
                        }
                    } else if ((geStart - 5) <= mapStart && (geEnd + 5) < mapEnd) {
                        if (s.hasNext()) {
                            line = s.nextLine();
                            check = 1;
                            break;
                        } else {
                            break;
                        }
                    } else if ((geStart - 5) > mapStart && (geEnd + 5) < mapEnd) {
                        if (s.hasNext()) {
                            line = s.nextLine();
                            check = 1;
                            break;
                        } else {
                            break;
                        }
                    } else {
                        if (i + 1 < gff_list.size()) {
                            i++;
                        } else {
                            break;
                        }
                        check = 1;
                        break;
                    }
                }
            }
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void parseArgs(final String[] args) {
        int i = 0;
        while (i < args.length) {
            try {
                switch (args[i].charAt(1)) { //check the switch
                    case 's':
                        SAM = new File(args[++i]);
                        break;
                    case 'g':
                        GFF = new File(args[++i]);
                        break;
                    case ('o'):
                        output = new File(args[++i]);
                        break;
                    default:
                        System.out.println("Invalid switch used");
                }
            } catch (Exception e) {
                System.err.println("Problem with: \"" + args[i] + "\".");
                e.printStackTrace();
            }
            i++;
        }
    }
}
