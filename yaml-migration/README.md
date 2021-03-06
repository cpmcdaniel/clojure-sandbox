# yaml-migration

A Clojure utility designed to allow migration from one YAML config file version to a newer version. The newer version may have new options and may have removed old ones which are now obsolete. This implementation has been made with the following assumptions and limitations:

1. The new file is assumed to be a sample containing all valid options with reasonable default values for the latest version of the configuration.
2. If an option exists in both files, the old value will be kept in the output.
3. If an option has been removed, it will be commented out at the bottom of the output.
4. If a new option has been added, it will be included in the output with its default value from the new file.
5. Comments, ordering, and other formatting is not currently preserved.
6. Does not currently handle deeply-nested changes (untested!).

## Usage

There are a few ways to execute this code.

### At the REPL

You can use ``lein repl`` in the root of the project to run the code interactively. The file paths are relative to the project root.

```clojure
(use 'yaml-migration.migrate)
(migrate-yaml "resources/file1.yaml" "resources/file2.yaml" "target/out.yaml")
```

### From the command line with lein

Alternatively, you can execute it from the command line with ``lein run``.

```
$ lein run -m yaml-migration.migrate resources/file1.yaml resources/file2.yaml target/out.yaml
```

### From the command line with a JAR

Finally, you could package everything up using ``lein uberjar`` and run it like so:

```
$ java -jar target/yaml-migration-0.1.0-SNAPSHOT-standalone.jar resources/file1.yaml resources/file2.yaml target/out.yaml
```

## Sample files

Sample input files can be found in ``resources``. A sample output file, ``out.yaml``, exists in the project root.

## Cassandra example

The Cassandra example takes a version to migrate to and uses Pomegranate/Aether to download the specified version of cassandra JAR file and depenencies into the local maven repo, adds them to the classpath, and then uses the Config class to compare against the old config file. To run this example:

```
$ lein run -m yaml-migration.cassandra resources/file1.yaml 2.0.6 target/out.yaml
```

## License

Copyright © 2014 Craig McDaniel

Distributed under the Eclipse Public License version 1.0.
