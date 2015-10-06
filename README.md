# web

Frontal web, permettant d'effectuer un devis.

Composant web de la démo GoCD-Ansible. Pour plus d'information voir [cd-infrastructure](https://github.com/snicaise/cd-infrastructure)

# Usage

Prérequis : maven 3 et java 8

Packaging
```sh
mvn clean package
```

Execution
```sh
cd web-core
java -jar target/web-core.jar server server.yml
curl -v http://localhost:8080/api/booking/quotation?origin=paris&destination=londres
```

# Tests

Executer les tests unitaire : 
```sh
mvn test
```

Executer les tests composant :
```sh
mvn test -Pcomponent-tests
```

Executer les tests d'intégration :
```sh
mvn test -Pintegration-tests
```

Executer tous les tests :
```sh
mvn test -Pall-tests
```
