localhost application
http://localhost:8080/api/news

http://localhost:8080/actuator
http://localhost:8080/actuator/prometheus

docker-compose up

prometheus http://localhost:9090

grafana http://localhost:3000
admin/admin

### Change grafana user password
docker exec -it de0821873f62 grafana-cli admin reset-admin-password admin


### use
http://localhost:8080/actuator/prometheus
temperature and mytimegauge and news_fetch_request_total

http://localhost:9090/graph?g0.expr=news_fetch_request_total&g0.tab=1&g0.stacked=0&g0.show_exemplars=0&g0.range_input=1h&g1.expr=temperature&g1.tab=1&g1.stacked=0&g1.show_exemplars=0&g1.range_input=1h&g2.expr=mytimegauge_seconds&g2.tab=1&g2.stacked=0&g2.show_exemplars=0&g2.range_input=1h
