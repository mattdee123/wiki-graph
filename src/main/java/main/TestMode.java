package main;

public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    for (String s : args) {
      int i = Integer.parseInt(s);
      System.out.printf("%d formats to %s%n", i, String.format("%015d", Math.abs(i)));
    }
  }
}
