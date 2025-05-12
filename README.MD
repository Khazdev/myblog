Spring Web Demo

Описание

Это веб-приложение на основе Spring MVC, которое позволяет пользователям создавать, просматривать, редактировать и удалять посты, 
добавлять комментарии, лайкать посты и использовать теги для категоризации. 
Приложение использует Spring Data JDBC для работы с базой данных и Apache Tomcat как сервлет-контейнер.

Требования

Java: JDK 21 
Maven: 3.6.0 или выше
Apache Tomcat: 10.0 или выше
База данных: H2

Установка

H2 база данных используется автоматически (настраивается в
src/main/resources/application.properties и
src/test/resources/test-application.properties для тестов).


Установите зависимости:
mvn clean install


Сборка приложения
Для сборки приложения в WAR, который можно развернуть в Tomcat:

mvn clean package

После успешной сборки WAR-файл появится в директории target, например: target/spring-web-demo.war.
Этот архив необходимо перенести в папку webapps Tomcat. пример: \apache-tomcat-10.1.40\webapps

Запуск тестов
mvn test

Запуск приложения
http://localhost:8080/spring-web-demo