docker build -t custom-spring-mysql .
docker run --name SpringMysql -e MYSQL_ROOT_PASSWORD=springmysql -p 33312:3306 -d custom-spring-mysql
