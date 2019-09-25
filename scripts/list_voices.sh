#!/bin/bash
# No inputs

echo `echo "(voice.list)" | festival -i | grep "festival> (" | cut -d "(" -f2 | cut -d ")" -f1`

