##Сборка проекта
./gradlew clean build

##Запуск проекта
./gradlew clean bootRun

##Пример запроса (input.json в находится корне проекта)
curl -i -X POST -H "Content-type: application/json" -d @input.json http://127.0.0.1:8080/api/quotes