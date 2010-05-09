#! /bin/bash

echo "[MEMSIM START]"

if [[ ! -f "memsim_java.jar" ]]; then
	if [[ ! -f "../../dist/memsim_java.jar" ]]; then 
		echo "A főszimulátor: \"memsim_java.jar\" nem található!"
		echo "[FATAL ERROR]"
		exit 1
	else
		cp ../../dist/memsim_java.jar .
	fi
fi

if [[ ! -f "configurations.list" ]]; then
	echo "A tesztesetek file-ja: \"configurations.list\" nem található!"
	echo "[FATAL ERROR]"
	exit 1
fi

if [[ ! -f "instructions.txt" ]]; then
	if [[ ! -f "instructions.txt" ]]; then
		echo "Az instrukciók file-ja: \"instructions.txt\" nem található"
		echo "[FATAL ERROR]"
		exit 1
	fi
fi

awk '
$0 !~ /^\#/ {
	print "java -jar memsim_java.jar "$0
}
' configurations.list > configurations.mod

i=1;
while [[ 1 ]]; do
	if [[ ! -d "memsim_run_$i" ]]; then
		break
	else 
		(( i += 1 ))
	fi
done

echo "[RUN $i]"

dir="memsim_run_$i"
j=0
inputlist=""

mkdir -p $dir/log
cp configurations.list $dir/

while read line; do
	echo -e "`$line`" > $dir/log/log$j.log
	mv export.csv "$dir/csv$j.csv"
	inputlist=$inputlist" $dir/csv$j.csv"
	(( j += 1 ))
done < configurations.mod

rm configurations.mod
cp instructions.txt $dir/

awk '
	BEGIN {
		count = 0;
		FS = ",";
		ki = "'$dir"/table.csv"'";
	}

	($0 ~ /^[A-Z]+/ && count == 0) {
		print ","$0 > ki;
		count++;
	}

	($0 !~ /^[A-Z]+/) {
		print "Config"count++","$0 > ki;
	}
' $inputlist

for (( j=j-1; j>=0; j-- )); do 
	rm $dir/csv$j.csv
done
echo "[SUCCESSFUL]"
exit 0
