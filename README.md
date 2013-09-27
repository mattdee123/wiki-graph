wiki-graph
==========

To run use ./run-wikigraph.sh [RUNMODE] [ARGS]

The runmodes are as follows:

-`site [BaseDir]`
The main run-mode.  BaseDir is where all of the wikipedia stuff is stored.

-`test`
Basically just a free main class for testing.  need a quick test?  java doesn't have an interpreter, so a good way to
test a few lines is to replace TestMode's run function

-`slice [location of Wikipedia XML] [output dir]`
"slices" up the giant wikipedia text into sub-directories, making a suitable BaseDir.

-`test-parse [string]` (hint:use quotes.  this must be all one argument!)
shows the results of parsing the given string

-`connections [baseDir] [article title]`
shows the connections on an article, according to data in the baseDir

-`test-parse [fileName]`
Like test-parse, but takes a file name
