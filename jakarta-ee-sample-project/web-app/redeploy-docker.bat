docker cp build/libs/web-app.war jakarta-ee-web-app:/tmp/
docker exec jakarta-ee-web-app /opt/jboss/wildfly/bin/jboss-cli.sh --connect --command="deploy /tmp/web-app.war --force"
