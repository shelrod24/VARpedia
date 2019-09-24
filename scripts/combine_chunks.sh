#!/bin/bash

# Combines audio files into audio with inside temps folder
# Will automatically overwrite
# First input is the name of the audio files to combine, with extension (eg "chunk1.wav chunk2.wav chunk3.wav")
# Author: Leon Chin (lchi184)

AUDIO_DIR="./temps/audio"
FINAL_DIR="./temps/final"
mkdir -p $AUDIO_DIR
mkdir -p $FINAL_DIR

# chnage directory to audio
cd $AUDIO_DIR

# combine audio files into temp folder
# will automatically overwrite
sox --combine concatenate ${1} "./TEMP.wav"

# cd to original folder
cd ..
cd ..

# move TEMP to FINAL_DIR
mv "${AUDIO_DIR}/TEMP.wav" "${FINAL_DIR}/TEMP.wav"

# also made an mp3 for reasons
ffmpeg -y -i "${FINAL_DIR}/TEMP.wav" "${FINAL_DIR}/TEMP.mp3" &> /dev/null
