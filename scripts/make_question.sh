#!/bin/bash

# Makes a creation that consists of audio and video components which are in the folder temps
# Creation is placed inside creations folder
# First input is the filename of the creation
# Second input is the term of the creation
# Author: Leon Chin (lchi184)

FINAL_DIR="./temps/final"
QUESTIONS_DIR="./questions"
mkdir -p ${FINAL_DIR}
mkdir -p ${QUESTIONS_DIR}

# getting duration of audio file
DURATION=`ffprobe -i "${FINAL_DIR}/TEMPREDACTEDFINAL.mp3" -show_entries format=duration -v quiet -of csv="p=0"`
# basically floors the value, then adds 1
DURATION=`printf "%.0f" ${DURATION}`
let DURATION++

# making the final creation by mixing audio with the short temp video (which has a lenght of generally 1 second) by extending the length by the duration
ffmpeg -y -t ${DURATION} -i "${FINAL_DIR}/TEMPREDACTEDFINAL.mp3" -i "${FINAL_DIR}/TEMPSHORTREDACT.mp4" -vf "setpts=${DURATION}*PTS" -strict -2 "${QUESTIONS_DIR}/${1}_${2}.mp4"
