package main;

import wikidump.WikiDumpSlicerMode;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the main class.
 */
public class Main {

  public static void main(String[] args) {

    Map<String, RunMode> modes = new HashMap<String, RunMode>();
    modes.put("site", new RunWebSiteMode());
    modes.put("slice", new WikiDumpSlicerMode());
    modes.put("parse", new ParseMode());
    modes.put("test", new TestMode());
    modes.put("connections", new ConnectionsMode());

    if (args.length < 1) {
      System.out.println("No run mode given: possible run modes are:" + modes.keySet());
      System.exit(1);
    }

    String[] otherArgs = new String[args.length - 1];
    System.arraycopy(args, 1, otherArgs, 0, args.length - 1);

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