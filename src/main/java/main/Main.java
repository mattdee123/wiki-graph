package main;

import wikidump.WikiDumpSlicerMode;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the main class.
 */
public class Main {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("No run mode given");
      System.exit(1);
    }

    Map<String, RunMode> modes = new HashMap<String, RunMode>();
    modes.put("site", new RunWebSiteMode());
    modes.put("slice", new WikiDumpSlicerMode());
    modes.put("parse", new ParseMode());

    String[] otherArgs = new String[args.length - 1];
    for (int i = 1; i < args.length; i++) {
      otherArgs[i - 1] = args[i];
    }

    RunMode runMode = modes.get(args[0]);
    if (runMode != null) {
      System.out.printf("Running with mode: %s%n", args[0]);
      runMode.run(otherArgs);
    }
    else {
      System.out.printf("No mode found matching: %s%n", args[0]);
      System.exit(1);
    }
  }
}