set MAMMOTH_TEST_SITE=https://%SERVER_PREFIX%.mammoth.io/
mvn clean test -Dsurefire.suiteXmlFiles=testSelenium.xml