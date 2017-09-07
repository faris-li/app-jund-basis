FROM 192.168.25.52/pltdev/vpbasic:latest

MAINTAINER security-service "killer_duan@vprisk.com"
ENV REFRESHED_AT 2017-06-06

ADD app-cloud-security-service-1.0.0.jar /usr/local/app-cloud-security-service-1.0.0.jar

CMD [ "java","-jar","/usr/local/app-cloud-security-service-1.0.0.jar" ]

EXPOSE 80