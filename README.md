# Spring Boot Template - RESTful API2

Simple Template for Spring RESTfull Service with Contract Driven Development (Swagger)

### Generate Code from Swagger file ###

NOTE: First thing first, add *README.md* and *pom.xml* in the __.swagger-codegen-ignore__ file. Otherwise, your README.md will be replaced by Swagger Code Gen. !!!

Add the following code in gradle script file.

```
buildscript {
    ...
    dependencies {
    ...
        classpath('io.swagger:swagger-codegen:2.2.2')
    }
}
...
configurations {
  generatedCompile
}
...
sourceSets {
  generated {
    compileClasspath = configurations.generatedCompile
  }
  main {
    compileClasspath += generated.output
    runtimeClasspath += generated.output
  }
  test {
    compileClasspath += generated.output
    runtimeClasspath += generated.output
  }
}

dependencies {
...
    compile('io.springfox:springfox-swagger2:2.9.2')        // To usw Swagger
...
    generatedCompile 'org.springframework.boot:spring-boot-starter-data-rest'
    generatedCompile 'io.springfox:springfox-swagger2:2.9.2'
    generatedCompile 'io.springfox:springfox-swagger-ui:2.9.2'
}

//--- Swagger Code Gen ---------------------------------
import io.swagger.codegen.config.CodegenConfigurator
import io.swagger.codegen.DefaultGenerator

def swaggerSourceFile = 'src/main/resources/person-swagger-api.json'
def swaggerTargetFolder = 'src/generated/java'
 
clean.doFirst {
  delete("${projectDir}/$swaggerTargetFolder")
}

task generateApi {
  doLast {
    def config = new CodegenConfigurator()
    config.setInputSpec("file:///$projectDir/$swaggerSourceFile")
    config.setOutputDir("$projectDir")
    config.setLang('spring')
    config.setAdditionalProperties([
        'interfaceOnly' : 'true',
        'apiPackage'    : 'brian.temp.spring.boot.api',
        'modelPackage'  : 'brian.temp.spring.boot.model',
        'sourceFolder'  : swaggerTargetFolder
    ])
    new DefaultGenerator().opts(config.toClientOptInput()).generate()
  }
}
..
compileGeneratedJava.dependsOn generateApi
classes.dependsOn generatedClasses
compileJava.dependsOn compileGeneratedJava
...
bootRun {
    classpath += sourceSets.generated.output
}

jar {
    from sourceSets.generated.output
}

```

Above code in the Gradle script will let your Gradle command to clean and generate Java code from Swagger file.

```
    ./gradlew clean generateApi
```
### Alternative way ###
You can use Swagger Code Gen executable java application. For this example, I've download the swagger-codegen.cli.jar into `docs` directory. You can run the following command from the root directory of the project.

```
java -jar docs/swagger-codegen-cli.jar generate -i src/main/resources/person-swagger-api.json -l spring -o output
```

* -i : Swagger API file in JSON format
* -l : Language - in this case, Spring
* -o : The directory where the generated code will be placed

From the generated code, *Controller.java files are where you can put your logics in.

### Optional
There are automatic logging mechanism included under `brian.example.boot.rest.config.logging`

# References #
* [https://blog.jcore.com/2017/05/22/automatically-generating-api-using-swagger-and-gradle/](https://blog.jcore.com/2017/05/22/automatically-generating-api-using-swagger-and-gradle/)
* [https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#fixed-fields-7](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#fixed-fields-7)
* [https://swagger.io/docs/specification/about/](https://swagger.io/docs/specification/about/)
