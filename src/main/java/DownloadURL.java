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

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * A poor, barebones impression of wget. :-)
 *
 * @author Curtis Rueden
 */
public class DownloadURL {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println("Please specify a URL as an argument.");
      System.exit(1);
    }
    String urlPath = args[0];
    URL url = new URL(urlPath);
    String filename = null;
    if (args.length > 1) filename = args[1];
    else {
      int slash = urlPath.lastIndexOf("/");
      int end = urlPath.lastIndexOf("?");
      if (end < 0) end = urlPath.length();
      filename = urlPath.substring(slash + 1, end);
      if (filename.equals("")) filename = "index.html";
    }
    System.out.println("Downloading " + urlPath + " to " + filename);
    InputStream in = url.openStream();
    FileOutputStream out = new FileOutputStream(filename);
    byte[] buf = new byte[65536];
    while (true) {
      int r = in.read(buf);
      if (r <= 0) break;
      out.write(buf, 0, r);
    }
    out.close();
    in.close();
  }

}
