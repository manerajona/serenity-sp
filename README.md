# Serenity JUnit Starter project

Get started quickly with Serenity BDD and JUnit 5 with this simple starter project. 

## Get the code

Click on the [Use This Template button](https://github.com/serenity-bdd/serenity-junit-starter/generate) to create a new project in your own GitHub account. 

Or simply [download a zip](https://github.com/serenity-bdd/serenity-junit-starter/archive/master.zip) file.

## Running the tests under Maven

The template project comes with both Maven and Gradle build scripts. To run the tests with Maven, open a command window and run:

    mvn clean verify -Pdev

## Use Gradle

For gradle, pen a command window and run:

    gradlew test 

## Viewing the reports

Both of the commands provided above will produce a Serenity test report in the `target/site/serenity` directory. Go take a look!

# ResReq MockServer

To run the tests locally, this project includes a MockServer for mocking requests using wiremock.

For further information about how Wiremock works, please visit https://wiremock.org/.

## How do I get set up?

This component is into a docker container. You need to generate a docker image using the command below.

```shell
docker build . --tag reqres/mockserver
```

Run the generated image with:

```shell
docker run -ti --rm -p 5005:8080 --name mockserver reqres/mockserver
```