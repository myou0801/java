# JBoss/WildFly コンテナイメージを使用
FROM quay.io/wildfly/wildfly

# アプリケーションの WAR ファイルをコンテナ内のデプロイメントディレクトリにコピー
COPY build/libs/web-app.war /opt/jboss/wildfly/standalone/deployments/

# ポート8080を公開
EXPOSE 8080
EXPOSE 9990

RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin

# WildFlyサーバーを起動
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
