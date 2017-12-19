# course-mailer

[![Build Status](https://travis-ci.org/HidekazuMasuda/course-mailer.svg?branch=master)](https://travis-ci.org/HidekazuMasuda/course-mailer)

# production run 
-Dspring.profiles.active=prod

# run localhost

gradle build
java -jar -Dspring.profiles.active=prod build/libs/course-mailer-0.0.1-SNAPSHOT.jar

## on windows

gradlew build
java -jar -Dspring.profiles.active=prod build\libs\course-mailer-0.0.1-SNAPSHOT.jar
