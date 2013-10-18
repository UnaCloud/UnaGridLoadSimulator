#!/bin/sh

export ValorInicio=1
export ValorFinal=1000

while test $ValorInicio -le $ValorFinal
do
 condor_submit submit.sub
 let ValorInicio=ValorInicio+1
done

echo $ValorInicio
echo $ValorFinal 
