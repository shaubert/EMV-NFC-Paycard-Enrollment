CALL mvn deploy:deploy-file -Durl=file://W:/home/maven-repo/releases -DrepositoryId=maven-repo -Dfile=./target/emv-paycard-library-2.1.3.jar -DgroupId=com.github.shaubert.emvnfccard -DartifactId=library -Dversion=2.1.3 -Dpackaging=jar -Dsources=./target/emv-paycard-library-2.1.3-sources.jar
PAUSE