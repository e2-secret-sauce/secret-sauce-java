### How to run the secret sauce

Compile the code
```bash
mvn clean package
```

#### Usage

Encrypt and Publish
```bash
java -jar -Dspring.profiles.active=local target\secretsauce-1.0.0-SNAPSHOT.jar publish travel_data.csv
```

Download and Analyze
```bash
java -jar -Dspring.profiles.active=local target\secretsauce-1.0.0-SNAPSHOT.jar analyze
```

#### Examples
Run it using locally generated keys

```bash
java -jar -Dspring.profiles.active=local target\secretsauce-1.0.0-SNAPSHOT.jar
```

Run it using retrieving keys from AWS KMS

```bash
java -jar -Dspring.profiles.active=aws target\secretsauce-1.0.0-SNAPSHOT.jar
```



View the encrypted data set 

```bash
src\main\resouces\encrypted_dataset.xls
```