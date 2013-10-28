package com.wikigraph.main;

import com.google.common.hash.Hashing;

import static com.google.common.base.Charsets.UTF_8;

public class HashMode implements RunMode {
  @Override
  public void run(String[] args) {
    for (String s : args) {
      System.out.println("Input:" + s);
      System.out.println(Hashing.sha256().hashString(s, UTF_8).asLong());
    }
  }
}
