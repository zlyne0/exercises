FROM lpktrc01:82/axa-mule:3.9.0-ce

ARG http_proxy=http://trac:3128
ARG https_proxy=http://trac:3128

RUN wget -S -Y on https://github.com/juliusv/prometheus_demo_service/releases/download/0.0.4/prometheus_demo_service-0.0.4.linux.386.tar.gz

# application service port
EXPOSE 18081

