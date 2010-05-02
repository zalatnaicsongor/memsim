package memsim_java;
import cache.algorithm.*;
import java.util.*;
import java.io.*;
import virtmemory.algorithm.*;
/**
 *
 * @author zalatnaicsongor
 */
public class Main {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Szerkezet:
        //args-ban kap egy file-t, amiber a műveletek előre le vannk generálva
        //strategy-ket, hogy ebben a futásban mit használ
        //mem, cache és virt mértetket
        //ezek alapján lefut, és a végén a statisztikát adott formában file-ba írja
        //ebből egy másik osztály/program/script grafikont csinálhat...

        if (args.length != 9) {
            System.out.println("Hibás paraméterlista!");
            System.exit(1);
        }

        //memória belövése
        int mem_addresslength = 0;
        int mem_physaddresslength = 0;
        int mem_pagesize = 0;
        try {
            mem_addresslength = Integer.parseInt(args[0]);
            mem_physaddresslength = Integer.parseInt(args[1]);
            mem_pagesize = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Helytelen memória paraméterek!");
            System.exit(1);
        }

        String a = args[3];
        PageReplaceStrategy prs = null;
        if (a.equals("Aging")) {
            prs = PageReplaceAging.getInstance();
        } else if (a.equals("FIFO")) {
            prs = PageReplaceFIFO.getInstance();
        } else if (a.equals("LRU")) {
            prs = PageReplaceLRU.getInstance();
        } else if (a.equals("NFU")) {
            prs = PageReplaceNFU.getInstance();
        } else if (a.equals("NRU")) {
            prs = PageReplaceNRU.getInstance();
        } else if (a.equals("Random")) {
            prs = PageReplaceRandom.getInstance();
        } else if (a.equals("SecondChance")) {
            prs = PageReplaceSecondChance.getInstance();
        } else {
            System.out.println("Hibás PageReplaceStrategy!");
            System.exit(1);
        }

        Memory.createMemory(mem_addresslength, mem_physaddresslength, mem_pagesize, prs);
        Memory memoria = Memory.getInstance(); // debugger miatt
        VirtMemory virtMem = VirtMemory.getInstance(); //ez is debugger miatt
        //memória belövésének vége

        //cache belövése
        int cache_rowsize = 0;
        int cache_numrows = 0;
        int cache_assoc = 0;
        try {
            cache_rowsize = Integer.parseInt(args[4]);
            cache_numrows = Integer.parseInt(args[5]);
            cache_assoc = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            System.out.println("Hibás cache értékek!");
            System.exit(1);
        }
        try {
            Cache.create(cache_rowsize, cache_numrows, cache_assoc);
        } catch (Exception e) {
            System.out.println(e);
        }
        Cache cache = Cache.getInstance(); //debugger miatt
        //cache belövésének vége

        //algoritmusok beállítása

        String a7 = args[7];
        CacheRowDiscardStrategy dis = null;
        if (a7.equals("First")) {
            dis = CacheRowDiscardFirst.getInstance();
        } else if (a7.equals("LFU")) {
            dis = CacheRowDiscardLFU.getInstance();
        } else if (a7.equals("LRU")) {
            dis = CacheRowDiscardLRU.getInstance();
        } else if (a7.equals("MFU")) {
            dis = CacheRowDiscardMFU.getInstance();
        } else if (a7.equals("MRU")) {
            dis = CacheRowDiscardMRU.getInstance();
        } else {
            System.out.println("Hibás cache discard strategy!");
            System.exit(1);
        }
        Cache.getInstance().setRowDiscardStrategy(dis);

        String a8 = args[8];
        CacheWriteStrategy wri = null;
        if (a8.equals("Allocator")) {
            wri = CacheWriteAllocator.getInstance();
        } else if (a8.equals("Back")) {
            wri = CacheWriteBack.getInstance();
        } else if (a8.equals("Through")) {
            wri = CacheWriteThrough.getInstance();
        } else {
            System.out.println("Hibás cache write strategy!");
            System.exit(1);
        }
        Cache.getInstance().setWriteStrategy(wri);
        //algoritmusok beállításának vége



        //segédváltozók
        Random rand = new Random();
        //segédváltozók vége

        //értelmező
        ArrayList<Pointer> p = new ArrayList<Pointer>();
        ArrayList<String> n = new ArrayList<String>();
        BufferedReader instructions = null;
        String line;
        try {
            instructions = new BufferedReader(new FileReader("instructions.txt"));
            while ((line = instructions.readLine()) != null) {
                try {
                    if (line.equals("") || line.matches("^\\s+$") || line.charAt(0) == '#') {
                        continue;
                    } else {
                        String[] pieces = line.split("\\s+");
                        if (pieces[0].equalsIgnoreCase("alloc")) {
                            if (n.indexOf(pieces[1]) != -1) {
                                System.out.println(pieces[1]+": Már van ilyen pointer!");
                                System.exit(1);
                            }
                            p.add(memoria.allocPointer(Integer.parseInt(pieces[2])));
                            n.add(pieces[1]);
                        } else if (pieces[0].equalsIgnoreCase("free")) {
                            int index;
                            if ((index = n.indexOf(pieces[1])) == -1) {
                                System.out.println(pieces[1]+": Nincs ilyen pointer!");
                                System.exit(1);
                            }
                            n.remove(index);
                            Pointer po = p.get(index);
                            p.remove(index);
                            po.free();
                        } else if (pieces[0].equalsIgnoreCase("read")) {
                            int index;
                            if ((index = n.indexOf(pieces[1])) == -1) {
                                System.out.println(pieces[1]+": Nincs ilyen pointer!");
                                System.exit(1);
                            }
                            Pointer po = p.get(index);
                            //itt a ret-tel nem csinálok semmit, csak van
                            int ret = po.read(Integer.parseInt(pieces[2]));
                        } else if (pieces[0].equalsIgnoreCase("write")) {
                            int index;
                            if ((index = n.indexOf(pieces[1])) == -1) {
                                System.out.println(pieces[1]+": Nincs ilyen pointer!");
                                System.exit(1);
                            }
                            Pointer po = p.get(index);
                            po.write(Integer.parseInt(pieces[2]),Integer.parseInt(pieces[3]));
                        } else {
                            System.out.println("Hibás paracs!");
                            System.exit(1);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Hiba: "+e);
                    System.exit(1);
                }
            }
            System.out.println("Szimuláció vége!");
        } catch (Exception e) {
            System.out.println("Hiba: " + e);
            System.exit(1);
        }
        //értelmező vége

        //csv exportálása
        Statistics.getInstance().exportCSV("export.csv");
        //kilépés
        System.exit(0);

    }
}
