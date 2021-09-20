FROM adoptopenjdk:11-jre-hotspot as build

WORKDIR /workspace/app

ADD settings.gradle settings.gradle

ADD gradlew gradlew
ADD gradle gradle

ADD common/build.gradle.kts common/build.gradle.kts
ADD common/settings.gradle.kts common/settings.gradle.kts
ADD common/src/main common/src/main

ADD listener/build.gradle.kts listener/build.gradle.kts
ADD listener/settings.gradle.kts listener/settings.gradle.kts
ADD listener/src/main listener/src/main

ADD rest-api/build.gradle.kts rest-api/build.gradle.kts
ADD rest-api/settings.gradle.kts rest-api/settings.gradle.kts
ADD rest-api/src/main rest-api/src/main

RUN ./gradlew assemble

FROM adoptopenjdk:11-jre-hotspot as listener

WORKDIR /workspace/app

VOLUME /tmp

COPY --from=build /workspace/app/listener/build/libs/app.jar app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /workspace/app/app.jar ${0} ${@}"]

FROM adoptopenjdk:11-jre-hotspot as rest-api

WORKDIR /workspace/app

VOLUME /tmp

COPY --from=build /workspace/app/rest-api/build/libs/app.jar app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /workspace/app/app.jar ${0} ${@}"]