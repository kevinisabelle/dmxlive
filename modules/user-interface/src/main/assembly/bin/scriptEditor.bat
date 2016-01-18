cd /d %~dp0

"java" -Xmx1024m -Djava.library.path=lib -jar lib/DmxLive-1.0.3.jar editor %1
