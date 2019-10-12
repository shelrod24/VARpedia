#!/bin/bash
# Deletes creation and corresponding question
# First input is the filename of the creationto be deleted

# remove creation
rm -f "./creations/${1}.mp4"

# get name of question
question=`echo ./questions/${1}_*.mp4`
rm -f "$question"
