FROM tomcat:8-jre8

RUN apt-get update 
	
COPY targer/amapj*.war /usr/local/tomcat/webapps/amapj.war

	