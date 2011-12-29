@echo Off
title Server
java -Xmx512m -XX:+DisableExplicitGC -noverify -cp bin;libs/* com.rs2.Server 127.0.0.1 43594 600
pause