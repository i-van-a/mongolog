To compile do:
- go to Logger.java
- set the desired database URL
- `sudo dnf install maven`
- `mvn clean package && mvn dependency:copy-dependencies`

To change the database 
- go to Logger.java
- set the desired database URL
- run first `mvn clean package && mvn dependency:copy-dependencies`
- rerun the project in burpsutie


- Set up database
- `docker run -v mongo-burp-project-x:/data/db -p <external-port>:27017  mongo`  Set `<external-port>` to 27017 if you didn't change the port in the url