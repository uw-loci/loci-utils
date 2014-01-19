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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Scans Java source files for unused and package imports.
 * Of course, Eclipse does this better.
 *
 * @author Curtis Rueden
 */
public class UnusedImports {
  public static void checkFile(String filename) throws IOException {
    BufferedReader file =
      new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
    Vector<String> imports = new Vector<String>();
    Vector<String> packages = new Vector<String>();

    String line = file.readLine();
    int lineNumber = 0;
    while (line != null) {
      line = line.trim();
      if (line.startsWith("import ")) {
        if (line.endsWith(".*;")) {
          packages.add(line.replace("import ", ""));
        }
        else {
          String className =
            line.substring(line.lastIndexOf(".") + 1, line.length() - 1);
          imports.add(className);
        }
      }
      else {
        for (int i=0; i<imports.size(); i++) {
          String importedClass = imports.get(i);
          if (line.indexOf(importedClass) != -1 && !line.startsWith("//") &&
            !line.startsWith("*") && !line.startsWith("/*"))
          {
            imports.remove(importedClass);
            i--;
          }
        }
      }

      line = file.readLine();
      lineNumber++;
    }

    if (imports.size() > 0 || packages.size() > 0) {
      System.out.println(filename);
      if (imports.size() > 0) {
        System.out.println("  Unused imports:");
        for (String importedClass : imports) {
          System.out.println("    " + importedClass);
        }
      }
      if (packages.size() > 0) {
        System.out.println("  Package imports:");
        for (String packageName : packages) {
          System.out.println("    " + packageName);
        }
      }
    }

    file.close();
  }

  public static void main(String[] args) throws IOException {
    for (String arg : args) {
      checkFile(arg);
    }
  }
}
