# mcembed
A proof-of-concept for Tomcat Embedded and mod_cluster. 

# Building

```
# git clone https://github.com/bostrt/modcluster-embedded.git
# cd modcluster-embedded
# mvn clean install
```

# Usage

You should already have your proxy (e.g. Apache HTTPD) configured before proceeding: 

```
# java -jar target/mcembed* 
```

There are some environment variables you can tweak:


- `PORT`: Configure the port that Tomcat listens on. Default is `8080`.
- `PROXY_HOST`: Configure the proxy hostname that mod_cluster attempts to connect to. Default is `localhost`.
- `PROXY_PORT`: Configure the proxy port that mod_cluster attempts to connect to. Default is `6666`.