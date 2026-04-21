FROM tomcat:10.1-jdk21

# Xóa app mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR của bạn vào ROOT
COPY dist/ql_thuoc_tay.war /usr/local/tomcat/webapps/ROOT.war

# Mở port
EXPOSE 8080

# Chạy Tomcat
CMD ["catalina.sh", "run"]