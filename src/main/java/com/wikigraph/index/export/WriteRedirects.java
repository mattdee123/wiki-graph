package com.wikigraph.index.export;

import com.google.common.base.Throwables;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class WriteRedirects {

  public void write(File inFile, File outFile) {
    try {
      System.out.println("Writing from " + inFile + " to " + outFile);
      BufferedReader reader = new BufferedReader(new FileReader(inFile));
      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
      int index = 0;
      String line;
      while ((line = reader.readLine()) != null) {
        int id = Integer.parseInt(line);
        while (index < id) {
          out.writeBoolean(false);
          index++;
        }
        out.writeBoolean(true);
        index++;
      }
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

}
