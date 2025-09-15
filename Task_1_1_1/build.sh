#!/bin/bash

rm -rf build

mkdir -p build/classes build/docs build/jar

javac -d build/classes src/main/java/ru/nsu/dizmestev/*.java

javadoc -d build/docs -sourcepath src/main/java -subpackages ru.nsu.dizmestev

jar -cf build/jar/SortLib.jar -C build/classes .