FROM debian:9.3-slim

RUN apt-get update && apt-get install -y apache2 libapache2-mod-wsgi

ADD https://bootstrap.pypa.io/get-pip.py /tmp
WORKDIR /tmp
RUN python get-pip.py
RUN pip install pipreqs

WORKDIR /var/www/sbeereck
RUN mkdir -p /var/www/sbeereck
ADD *.py /var/www/sbeereck/
ADD sbeereck.wsgi /var/www/sbeereck

RUN pipreqs .
RUN pip install -r requirements.txt 


RUN a2enmod ssl
ADD apache_conf_files/apache2.conf /etc/apache2/
ADD apache_conf_files/httpd-vhosts.conf /etc/apache2/sites-enabled/000-default.conf

CMD apache2 -DFOREGROUND -e info
