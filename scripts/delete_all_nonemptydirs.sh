#!/bin/bash

for DIR in ./audio/*; do

  if [ -d "$DIR" ]; then

    STRING=`ls -h "$DIR"`

    if [[ ! "$STRING" == *".wav"* ]]; then

     rmdir "$DIR/redacted"
     rmdir "$DIR"

    fi

  fi

done
