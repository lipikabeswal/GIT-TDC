cls
cd ..
@set CURPATH=%cd%
set JETTY_HOME=%CURPATH%\servletcontainer\jetty-5.1.11RC0
@rem set CLASSPATH=C:\apps\Java\jdk1.5.0_04\lib\tools.jar
set CLASSPATH=%CLASSPATH%;%JETTY_HOME%\lib\org.mortbay.jetty.jar
set CLASSPATH=%CLASSPATH%;%JETTY_HOME%\lib\javax.servlet.jar
set CLASSPATH=%CLASSPATH%;%JETTY_HOME%\ext\jasper-runtime.jar
set CLASSPATH=%CLASSPATH%;%JETTY_HOME%\ext\jasper-compiler.jar
set CLASSPATH=%CLASSPATH%;%JETTY_HOME%\ext\xercesImpl.jar
set JETTY_HOME
set CLASSPATH
java -Djetty.home=%JETTY_HOME% -jar -Dtdc.webapp=%CURPATH%\webapp %JETTY_HOME%\stop.jar %JETTY_HOME%\etc\tdc.xml
exit