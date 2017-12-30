# Useful commands and tips

Run the mysql container with detached storage :  
docker run --name sbeereck-mysql -v ./mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=toor -p 3306:3306 -d mysql:5.7
=> mysql will run on port 3306  
