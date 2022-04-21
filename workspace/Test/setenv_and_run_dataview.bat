set /P SERVER_PREFIX=<version.txt
set MAMMOTH_TEST_SITE=http://%SERVER_PREFIX%.mammoth.io/
mvn clean test -DsuiteXmlFile=testDataview.xml
