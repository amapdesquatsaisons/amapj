FROM debian:jessie

RUN echo "deb http://ftp.de.debian.org/debian stretch main" > /etc/apt/sources.list
RUN apt-get update 
	
RUN apt-get install -y apache2 wget libapache2-mod-neko curl imagemagick tar

RUN apt-get install haxe -y \
	&& mkdir ~/haxelib && haxelib setup ~/haxelib

RUN a2enmod neko \
	&& a2enmod rewrite
	
COPY config.xml /var/www/config.xml
COPY 000-default.conf /etc/apache2/sites-available/000-default.conf

RUN apt-get clean

ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2

EXPOSE 80

CMD ["/usr/sbin/apache2ctl", "-D", "FOREGROUND"]


	