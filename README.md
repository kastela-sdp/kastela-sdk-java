# KASTELA Server SDK for JAVA

## Related Link

- [API docs](https://kastela-sdp.github.io/kastela-sdk-java/id/hash/kastela/package-summary.html)
- [Packages](https://github.com/kastela-sdp/kastela-sdk-java/packages/1809924)
- [Guide to use Personal Access Token](https://github.com/jcansdale-test/maven-consume)

## Installation

1. Add to pom.mls (check the latest version [here](https://github.com/kastela-sdp/kastela-sdk-java/packages/1809462))

    ```java
    <dependencies>
      ...
      <dependency>
        <groupId>id.hash.kastela</groupId>
        <artifactId>kastela-sdk-java</artifactId>
        <version>0.5.0</version> 
      </dependency>
      ...
    </dependencies>

    <repositories>
      ...
      <repository>
        <id>github</id>
        <name>GitHub kastela-sdp Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/kastela-sdp/*</url>
      </repository>
      ...
    </repositories>
    ```

2. Run

    ```bash
    mvn install
    ```

## Usage Example

Credential is required when using the SDK, download it on the entities page.

``` java
// create new instance of Kastela Client
Client kastelaClient = new Client("https://127.0.0.1:3100", "credentials/client.crt", "credentials/client.key", "credentials/ca.crt");

// prepare input data
ArrayList<ProtectionOpenInput> input = new ArrayList<ProtectionOpenInput>();
input.add(new ProtectionOpenInput("your protection id", List.of("your token here", "token2").toArray()));

// read protected data
ArrayList<ArrayList<Object>> result = kastelaClient.protectionOpen(input);
System.out.println(result); 
```
