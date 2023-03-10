FROM wiremock/wiremock:2.35.0-alpine

COPY src/main/resources/mappings /home/wiremock/mappings
COPY src/main/resources/responses /home/wiremock/__files

EXPOSE 8080

CMD ["java", "-cp", "/var/wiremock/lib/*:/var/wiremock/extensions/*", "com.github.tomakehurst.wiremock.standalone.WireMockServerRunner","--global-response-templating", "--container-threads=20", "--async-response-enabled=true", "--async-response-threads=20", "--no-request-journal=true", "--disable-gzip=true"]