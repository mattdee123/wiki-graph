package com.wikigraph;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class LinkIndexWriter {

  public void writeAndClose(File inFile, File indexOut, File dataOut) {
    try {

      BufferedReader reader = new BufferedReader(new FileReader(inFile));
      DataOutputStream indexWriter = new DataOutputStream(new FileOutputStream(indexOut));
      DataOutputStream dataWriter = new DataOutputStream(new FileOutputStream(dataOut));

      int nextIndex = 0;
      System.out.printf("Writing info from %s to indexfile %s and datafile %s\n", inFile, indexOut, dataOut);


      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        String[] words = line.split(",");
        if (words.length != 2) {
          System.err.printf("Bad line: %2%n");
          continue;
        }
        int indexed = Integer.parseInt(words[0]);
        int target = Integer.parseInt(words[1]);
        if (indexed < nextIndex) {
          System.err.printf("Out of order index, exiting now: expected next index=%d, next index=%d%n",
                  nextIndex, indexed);
          return;
        }
        while (nextIndex <= indexed) {
          // Write the current pointer of the dataWriter to the index
          indexWriter.writeInt(dataWriter.size());
        }
        dataWriter.writeInt(target);
      }
      //Write end of data file here.
      indexWriter.writeInt(dataWriter.size());
      indexWriter.close();
      dataWriter.close();
      reader.close();
      System.out.println("Done Writing!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
