package com.wikigraph;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Indexer {

  private final DataSource dataSource;

  public Indexer(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void write(File in, File outDir) {
    try {
      outDir.mkdirs();
      File indexFile = new File(outDir, "index");
      File dataFile = new File(outDir, "data");
      indexFile.createNewFile();
      dataFile.createNewFile();
      DataOutputStream indexWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)));
      DataOutputStream dataWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));
      BufferedReader reader = new BufferedReader(new FileReader(in));

      int nextIndex = 1;
      System.out.printf("Indexing to indexfile %s and datafile %s%n", indexFile, dataFile);

      indexWriter.writeInt(dataWriter.size());
      String line;
      while (null != (line = reader.readLine())) {
        int index = dataSource.getIndexAndWriteData(dataWriter, line);
        if (index < nextIndex) {
          System.err.printf("Out of order index, exiting now: expected next index=%d, found index=%d%n",
                  nextIndex, index);
          return;
        }
        while (nextIndex <= index) {
          // Write the current pointer of the dataWriter to the index
          indexWriter.writeInt(dataWriter.size());
          nextIndex++;
        }
      }
      //Write end of data file here.
      indexWriter.close();
      dataWriter.close();
      System.out.println("Done Writing!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
