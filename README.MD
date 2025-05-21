Spring Boot Web Demo

Описание

Это веб-приложение на основе Spring Boot MVC, которое позволяет пользователям создавать, просматривать, редактировать и удалять посты, 
добавлять комментарии, лайкать посты и использовать теги для категоризации. 
Приложение использует Spring Data JDBC для работы с базой данных.

Требования


Java: JDK 21
Gradle: 8.13 или выше
База данных: H2


Установка


H2 база данных используется автоматически (настраивается в
src/main/resources/application.properties и
src/test/resources/test-application.properties для тестов).


Установите зависимости:
./gradlew build


Запуск тестов
./gradlew test 
Результат тестов можно посмотреть в html файле build/reports/tests/test/index.html


Запуск приложения
Для локального запуска достаточно запустить класс SpringBootWebDemoApplication
или ./gradlew bootRun
и приложение будет работать тут -> http://localhost:8080/