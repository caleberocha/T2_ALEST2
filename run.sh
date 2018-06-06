#!/bin/bash
set -e
[ ! -d build ] && mkdir build
javac -d bin src/pucrs/calebe/alest2/t2/*.java
jar cfe build/custo.jar pucrs.calebe.alest2.t2.App -C bin pucrs

for file in $(find data/caso*.txt)
do
	java -jar build/custo.jar $file
	echo
done
