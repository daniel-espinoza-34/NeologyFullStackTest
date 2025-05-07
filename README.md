# Full-Stack-Prueba-tecnica-Neology


Implementacion de la Prueba tecnica, [detalles sobre la prueba](https://github.com/VictorMC96/Full-Stack-Prueba-tecnica-Neology/blob/main/README.md#full-stack-prueba-tecnica-neology).

## Backend

La infomracion de prueba se genera de manera aleatoria al iniciar el proyecto, tanto para ejecuion normal como para pruebas, los detalles se encuentran en la clase **com.neology.parking.util.initializer.InitializeInfo**

Para ejecutar el proyecto spring desde la terminal

```
cd ./backend
mvn clean compile exec:java
```

Para ejecutar los test

```
cd ./backend
mvn clean test
```

## Frontend

Para ejecutar el proyecto angular es necesario ejecutar los siguientes comandos desde la terminal

```
cd ./frontend
npm install
npm start
```

Para ejecutar los tests

```
cd ./frontend
npm test
```