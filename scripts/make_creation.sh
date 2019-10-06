#!/bin/bash

# Makes a creation that consists of audio and video components which are in the folder temps
# Creation is placed inside creations folder
# First input is the name of the creation
# Author: Leon Chin (lchi184)

FINAL_DIR="./temps/final"
CREATIONS_DIR="./creations"
mkdir -p ${TEMPS_DIR}
mkdir -p ${CREATIONS_DIR}

# getting duration of audio file
DURATION=`ffprobe -i "${FINAL_DIR}/TEMP.mp3" -show_entries format=duration -v quiet -of csv="p=0"`
# basically floors the value, then adds 1
DURATION=`printf "%.0f" ${DURATION}`
let DURATION++

# making the final creation by mixing audio with the short temp video (which has a lenght of generally 1 second) by extending the length by the duration
ffmpeg -y -i "${FINAL_DIR}/TEMP.mp3"  -i "${FINAL_DIR}/TEMPSHORT.mp4" -vf "setpts=${DURATION}*PTS" -strict -2 "${CREATIONS_DIR}/${1}.mp4"
