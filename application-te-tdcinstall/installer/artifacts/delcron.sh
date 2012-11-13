crontab -l > tmp.txt
sed '/OAS Load Test/d' tmp.txt > tmp2.txt
crontab tmp2.txt
