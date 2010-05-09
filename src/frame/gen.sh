#! /bin/bash

function random {
	#min, max
	i=0
	while [[ $i -lt $1 ]]; do
		i=$(( $RANDOM % $2 ))
	done
	echo $i
}

counter=0

echo "alloc po 30100"
for (( ; ; )); do
	maxcikluskor=`random 20 40`
	for (( j=1; j<$maxcikluskor; j++ )); do
		kezd=`random 1 30000`
		plusz=$RANDOM%100
		vege=$(( $kezd + $plusz ))
		lepes=`random 1 5`
		for (( k=$kezd; $k<$vege; k=$(($k + $lepes)) )); do
			echo "read po $k"
			counter=$(( ++counter ))
		done
	done
	if [[ $counter -gt 10000 ]]; then 
		break 
	fi
done
echo "free po"



