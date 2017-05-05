package com.davidbalazs.watcher.poc;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class SecondApproach {

  public static void watchDirectoryPath(Path path) {
    System.out.println("Watching path: " + path);
    FileSystem fs = path.getFileSystem();
    try (WatchService service = fs.newWatchService()) {
      path.register(service, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
      WatchKey watchKey;
      while (true) {
        watchKey = service.take();
        for (WatchEvent event : watchKey.pollEvents()) {
          if (event.kind() == OVERFLOW) {
            System.out.println("OVERFLOW!!! " + event.context().toString());
          }
          if (event.kind() == ENTRY_CREATE) {
            System.out.println("Created: " + event.context().toString());
          }
          if (event.kind() == ENTRY_DELETE) {
            System.out.println("Delete: " + event.context().toString());
          }
          if (event.kind() == ENTRY_MODIFY) {
            System.out.println("Modify: " + event.context().toString());
          }
        }

        if (!watchKey.reset()) {
          break;
        }
      }

    } catch (IOException | InterruptedException ioe) {
      ioe.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException,
                                                InterruptedException {
    // Folder we are going to watch
    Path folder = Paths.get("tmp");
    watchDirectoryPath(folder);
  }
}
