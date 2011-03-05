//
// CompareTrees.java
//

/*
A collection of simple Java utilities.

Copyright (c) 2006, UW-Madison LOCI
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the UW-Madison LOCI nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

import java.io.File;
import java.util.Arrays;

/**
 * Compares the paths specified as command line arguments, outputting all
 * differences between them (i.e., files and directories present in one path
 * but not in the other). This implementation does note when two files do not
 * have matching file sizes, but does not check byte-for-byte equality when
 * the file sizes match.
 *
 * You can use GNU diff on two folders to accomplish a similar effect.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://dev.loci.wisc.edu/trac/java/browser/trunk/projects/utils/src/main/java/CompareTrees.java">Trac</a>,
 * <a href="http://dev.loci.wisc.edu/svn/java/trunk/projects/utils/src/main/java/CompareTrees.java">SVN</a></dd></dl>
 */
public class CompareTrees {

  public static final void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Please specify two paths to compare.");
      System.exit(1);
    }
    String path1 = args[0];
    String path2 = args[1];
    File dir1 = new File(path1);
    File dir2 = new File(path2);
    if (!dir1.exists() || !dir1.isDirectory()) {
      System.out.println(path1 + " is invalid.");
      System.exit(2);
    }
    if (!dir2.exists() || !dir2.isDirectory()) {
      System.out.println(path2 + " is invalid.");
      System.exit(3);
    }

    compare(dir1, dir2);
  }

  public static final void compare(File dir1, File dir2) {
    if (dir1 == null) {
    	if (dir2 == null) return;
      System.out.println(">>> " + dir2 + "\t[" +
        dir2.listFiles().length + " files]");
      return;
    }
    if (dir2 == null) {
      System.out.println("<<< " + dir1 + "\t[" +
        dir1.listFiles().length + " files]");
      return;
    }
    File[] list1 = dir1.listFiles();
    File[] list2 = dir2.listFiles();
    if (list1 == null) list1 = new File[0];
    if (list2 == null) list2 = new File[0];
    Arrays.sort(list1);
    Arrays.sort(list2);
    int ndx1 = 0, ndx2 = 0;
    while (ndx1 < list1.length && ndx2 < list2.length) {
      boolean d1 = list1[ndx1].isDirectory();
      boolean d2 = list2[ndx2].isDirectory();
      int c = list1[ndx1].getName().compareToIgnoreCase(list2[ndx2].getName());
      if (c < 0) {
        System.out.print("<<< " + list1[ndx1++] + "\t[missing]");
        if (d1) System.out.print(" [directory]");
        System.out.println();
      }
      else if (c > 0) {
        System.out.print(">>> " + list2[ndx2++] + "\t[missing]");
        if (d2) System.out.print(" [directory]");
        System.out.println();
      }
      else {
        if ((d1 && !d2)) System.out.println("!D! " + list2[ndx1]);
        else if (!d1 && d2) System.out.println("!D! " + list1[ndx1]);
        else if (d1 && d2) compare(list1[ndx1], list2[ndx2]);
        else {
          long len1 = list1[ndx1].length();
          long len2 = list2[ndx2].length();
          if (len1 != len2) {
            System.out.println("!S! " + list1[ndx1] +
              "\t[" + len1 + " vs " + len2 + "]");
          }
        }
        ndx1++;
        ndx2++;
      }
    }
    if (ndx1 < list1.length) {
      for (int i=ndx1; i<list1.length; i++) {
        boolean d1 = list1[i].isDirectory();
        System.out.print("<<< " + list1[i] + "\t[missing]");
        if (d1) System.out.print(" [directory]");
        System.out.println();
      }
    }
    if (ndx2 < list2.length) {
      for (int i=ndx2; i<list2.length; i++) {
        boolean d2 = list2[i].isDirectory();
        System.out.print(">>> " + list2[i] + "\t[missing]");
        if (d2) System.out.print(" [directory]");
        System.out.println();
      }
    }
  }

}
