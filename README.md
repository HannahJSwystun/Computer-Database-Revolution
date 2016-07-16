# Computer-Database-Revolution
Play Framework Java Jasper Report PDF XLS HTML MySQL Checkboxes Selection Javascript Ajax

# Application.conf >> NEW SCHEMA >> Create new schema in MySQL DB named :  "new" 
db.default.driver=com.mysql.jdbc.Driver    
db.default.url="jdbc:mysql://127.0.0.1:3306/new?characterEncoding=UTF-8"
db.default.username="root"
db.default.password="root"
db.default.jndiName=DefaultDS

# Application.conf >> PLAY MAILER CONFIG >> change SMTP infos 
play.mailer 
{

  host              =  smtp.gmail.com 
  port              =  465
  ssl               =  yes
  tls               =  no
  user              =  "xxxxxxxxx@gmail.com"
  password          =  "xxxxxxxxx"
  debug             =  yes
  timeout           =  60000  
  connectiontimeout =  60000
  mock              =  no
  
}

# Run Application
sbt run
