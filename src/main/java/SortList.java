/*
 * #%L
 * A collection of simple Java utilities.
 * %%
 * Copyright (C) 2006 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Sorts a text file alphabetically, removing duplicates.
 * <p>
 * You can use "sort file.txt | uniq" to accomplish the same thing.
 * </p>
 *
 * @author Curtis Rueden
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
