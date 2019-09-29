#!/bin/bash

rm -f "./temps/Linedtextfile.txt"
TEXTFILE='./temps/text.txt'
LINEDTEXTFILE='./temps/Linedtextfile.txt'
touch "./temps/Linedtextfile.txt"

while read line; do
	
    for word in $line; do 

        word=`echo -e "${word}" | tr -d '*([[:space:]])'`

        if [[ $temp =~ ^.*\.$ ]]; then
            echo $temp | tee -a $LINEDTEXTFILE
	    echo 
        else  
            echo -n "$temp "| tee -a $LINEDTEXTFILE
        fi

        temp="$word"

    done

    echo $word | tee -a $LINEDTEXTFILE

done < $TEXTFILE
