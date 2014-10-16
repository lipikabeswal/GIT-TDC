#!/bin/bash

# Curl command to start the project in CC
# stat=`curl -s "$JMXURL/invoke?objectname=CruiseControl+Project:name=$project&$params&template=identity" | sed -r 's/>/>\n/g' | grep '<Operation ' | cut -d '"' -f 6`

echo ""
echo "OAS Reminders"
echo "========================================================================"
echo " * Please include the submission number in JIRA or QualityCenter ticket."
echo " * Please be sure to note who performed your peer review."
echo ""
