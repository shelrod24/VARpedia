#!/bin/bash

# Combines audio files inside audio folder
# Also combines audio files inside redacted folder
# Will automatically overwrite
# First input is the folder to find audio
# Second input is the name of the audio files to combine, with extension (eg "chunk1.wav chunk2.wav chunk3.wav")
# Author: Leon Chin (lchi184)

AUDIO_DIR="./audio/${1}"
REDACTED_DIR="./audio/${1}/redacted"
FINAL_DIR="./temps/final"
mkdir -p "$AUDIO_DIR"
mkdir -p "$REDACTED_DIR"
mkdir -p "$FINAL_DIR"

# chnage directory to audio
cd "$AUDIO_DIR"

# combine audio files into temp folder
# will automatically overwrite
sox --combine concatenate ${2} "./TEMP.wav"

# change dir into redacted
cd "./redacted"

# replace . with _redacted. to get redacted filenames
redacted=`echo "${2}" | sed 's/\./_redacted./g'`
# make redacted audio
sox --combine concatenate ${redacted} "./TEMPREDACTED.wav"

# cd to original folder
cd ..
cd ..
cd ..

# move TEMP and TEMPREDACTED to FINAL_DIR
mv "${AUDIO_DIR}/TEMP.wav" "${FINAL_DIR}/TEMP.wav"
mv "${REDACTED_DIR}/TEMPREDACTED.wav" "${FINAL_DIR}/TEMPREDACTED.wav"

# also made an mp3 for reasons
ffmpeg -y -i "${FINAL_DIR}/TEMP.wav" "${FINAL_DIR}/TEMP.mp3" &> /dev/null
ffmpeg -y -i "${FINAL_DIR}/TEMPREDACTED.wav" "${FINAL_DIR}/TEMPREDACTED.mp3" &> /dev/null
