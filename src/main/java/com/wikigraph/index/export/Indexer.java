package com.wikigraph.index.export;

import com.google.common.base.Throwables;

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

      int lastIndex = -1;
      System.out.printf("Indexing to indexfile %s and datafile %s%n", indexFile, dataFile);

      String line;
      while (null != (line = reader.readLine())) {
        int pos = dataWriter.size();
        int index = dataSource.getIndexAndWriteData(dataWriter, line);
        // If the new index was before the previous one
        if (lastIndex > index) {
          System.err.printf("Out of order index, exiting now: expected >=%d, found index=%d%n",
                  lastIndex, index);
          return;
        }
        while (lastIndex < index) {
          // Write the current pointer of the dataWriter to the index
          lastIndex++;
          indexWriter.writeInt(pos);
        }
      }
      indexWriter.writeInt(dataWriter.size());
      //Write end of data file here.
      indexWriter.close();
      dataWriter.close();
      System.out.println("Done Writing!");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

}
