@set CURPATH=%cd%
@start /MIN tdc_start_svc.bat 
@"C:\Program Files\Internet Explorer\IEXPLORE.EXE" -k file://%CURPATH%/loading.html
@start /MIN tdc_stop_svc.bat