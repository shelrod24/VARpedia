#!/bin/bash

# Bascially just a tts
# First input is the text to be spoken
# Author: Leon Chin (lchi184)

echo "${1}" | festival --pipe --tts 
