//
// SortList.java
//

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Sorts a text file alphabetically, removing duplicates.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="https://skyking.microscopy.wisc.edu/trac/java/browser/trunk/utils/SortList.java">Trac</a>,
 * <a href="https://skyking.microscopy.wisc.edu/svn/java/trunk/utils/SortList.java">SVN</a></dd></dl>
 */
public class SortList {

  public static void main(String[] args) throws Exception {
    boolean showDups = args[0].equals("-v");
    String file = args[showDups ? 1 : 0];
    ArrayList<String> lineList = new ArrayList<String>();
    BufferedReader fin = new BufferedReader(new FileReader(file));
    while (true) {
      String line = fin.readLine();
      if (line == null) break;
      lineList.add(line);
    }
    fin.close();
    String[] lines = new String[lineList.size()];
    lineList.toArray(lines);
    Arrays.sort(lines);
    new File(file).renameTo(new File(file + ".old"));
    PrintWriter fout = new PrintWriter(new FileWriter(file));
    for (int i=0; i<lines.length; i++) {
      if (i == 0 || !lines[i].equals(lines[i-1])) fout.println(lines[i]);
      else if (showDups) System.out.println("Duplicate line: " + lines[i]);
    }
    fout.close();
  }

}
