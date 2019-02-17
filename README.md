### How to run the secret sauce

Compile the code
```bash
mvn clean package
```

Run it using hardcoded keys

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