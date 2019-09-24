#!/bin/bash

IMAGE_DIR="./temps/image"
mkdir -p "${IMAGE_DIR}"
cd "${IMAGE_DIR}"

curl "https://www.flickr.com/search/?text=${1}" 2> /dev/null| grep -oh '//live.staticflickr.com/.*jpg' | sed 's/^/http:/' | head -$2 | wget -i- &> /dev/null
