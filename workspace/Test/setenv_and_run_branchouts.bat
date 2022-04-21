set /P SERVER_PREFIX=<version.txt
set MAMMOTH_TEST_SITE=https://%SERVER_PREFIX%.mammoth.io
mvn clean test -DsuiteXmlFile=testBranchouts.xml
