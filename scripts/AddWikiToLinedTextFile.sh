#!/bin/bash

rm -f Linedtextfile.txt
TEXTFILE='./text.txt'
LINEDTEXTFILE='./Linedtextfile.txt'
touch Linedtextfile.txt

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
