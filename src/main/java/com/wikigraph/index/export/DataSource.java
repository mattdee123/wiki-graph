package com.wikigraph.index.export;

import java.io.DataOutputStream;

public interface DataSource {

  public int getIndexAndWriteData(DataOutputStream outputStream, String source);

}
