#!/bin/bash

FESTIVAL_DIR="./temps/festival"
mkdir -p $FESTIVAL_DIR

cd $FESTIVAL_DIR

echo "(voice.list)" > voicelist.scm

echo `festival -b voicelist.scm`


