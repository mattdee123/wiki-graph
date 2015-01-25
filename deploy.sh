#!/bin/bash
forever start -c "java -Xmx2000M -jar" target/wiki-graph-1.0-SNAPSHOT.jar site /home/ubuntu/index
