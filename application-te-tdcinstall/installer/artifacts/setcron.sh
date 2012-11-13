crontab -l > tmp.txt
echo "*/10 * * * * open -a \""$1"/OAS Load Test.app\"" >> tmp.txt
crontab tmp.txt
