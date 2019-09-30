#!/bin/bash

for DIR in ./audio/*; do

  if [ -d "$DIR" ]; then

    STRING=`ls -h audio/$DIR`

    if [ "$STRING" == "" ]; then

     rmdir  "$DIR"

    fi

  fi

done