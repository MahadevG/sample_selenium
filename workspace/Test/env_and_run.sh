export SERVER_PREFIX=`cat version.txt`
export MAMMOTH_TEST_SITE=https://${SERVER_PREFIX}.mammoth.io/
mvn test site
