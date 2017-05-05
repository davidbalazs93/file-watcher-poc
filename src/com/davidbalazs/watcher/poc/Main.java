package com.davidbalazs.watcher.poc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class Main {

  public static void main(String[] args) {

    //define a folder root
    Path myDir = Paths.get("tmp");

    try {
      WatchService watcher = myDir.getFileSystem().newWatchService();
      myDir.register(watcher,
                     StandardWatchEventKinds.ENTRY_CREATE,
                     StandardWatchEventKinds.ENTRY_DELETE,
                     StandardWatchEventKinds.ENTRY_MODIFY);

      WatchKey watckKey;
      while (true) {
        System.out.println("Watching dir for changes...");
        watckKey = watcher.take();
        System.out.println("Event occured:");
        for (WatchEvent event : watckKey.pollEvents()) {
          if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
            System.out.println("Created: " + event.context().toString());
          }
          if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
            System.out.println("Delete: " + event.context().toString());
          }
          if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
            System.out.println("Modify: " + event.context().toString());
          }
        }
      }

    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
    }
  }
}
