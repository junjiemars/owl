Owl - web front
===========

## Requirements
1. [clojure1.6+](https://github.com/clojure/clojure.git)
2. [lein2.2+](https://github.com/technomancy/leiningen.git)

## How to build
* Clean the generated JavaScript file:
```sh
rm resources/public/js/main.js
```
* Build for Debug:
```sh
DEBUG=1 lein with-profiles dev cljsbuild once
```
* Build for Production:
```sh
DEBUG=1 lein with-profiles pro cljsbuild once
```

## Easy to use
Open Owl popup window > input Proxy URL > click Run
* for Socks:
```URL
socks5://localhost:8080
```
* for http:
```URL
http://localhost:8080
```
* for ftp:
```URL
ftp://localhost:8080
```

## About SSH Port Forwarding
* On mac osx: 
```sh
ssh -vND<same-port-with-remote> <user>@host
```
* On windows: 
[tunnelier](http://www.bitvise.com/tunnelier)

* On ubuntu: 
```sh
ssh -v -D<same-port-with-remote> <user>@host
```

