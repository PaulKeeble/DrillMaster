set dirname=%~dp0

java -Dconfig.resource=prod.conf -Dhttp.port=80 %* -cp "%dirname%/lib/*" play.core.server.NettyServer %dirname%

pause