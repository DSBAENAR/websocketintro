## Getting Started

1. Cree la siguiente clase que iniciará el servidor de aplicaciones de Spring

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebSocketIntroApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketIntroApplication.class, args);
    }

}
```

2. Cree un controlador Web que le permitirá cargar la configuración mínima Web-MVC

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@GetMapping("/status")
    public String status(){
        return "{\"status\":\"Greetings from SpringBoot " 
        + LocalDateTime.now()
        .atZone(ZoneId.of("UTC-5"))
        .format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm")) 
        + " "
        + "The server is Running \"}";
    }
```

3. Cree un index html en la siguiente localización: /src/main/resources/static
![alt text](/readme/img/image.png)

4. Corra la clase que acabamos de crear y su servidor debe iniciar la ejecución

```bsh

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::       (v4.0.0-SNAPSHOT)

2025-10-25T09:50:21.952-05:00  INFO 24611 --- [websocketintro] [           main] c.w.w.WebsocketintroApplication          : Starting WebsocketintroApplication using Java 17.0.12 with PID 24611 (/Users/dsbaenar/development/websocketintro/target/classes started by dsbaenar in /Users/dsbaenar/development/websocketintro)
2025-10-25T09:50:21.954-05:00  INFO 24611 --- [websocketintro] [           main] c.w.w.WebsocketintroApplication          : No active profile set, falling back to 1 default profile: "default"
2025-10-25T09:50:22.238-05:00  INFO 24611 --- [websocketintro] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
2025-10-25T09:50:22.243-05:00  INFO 24611 --- [websocketintro] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-10-25T09:50:22.243-05:00  INFO 24611 --- [websocketintro] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.13]
2025-10-25T09:50:22.262-05:00  INFO 24611 --- [websocketintro] [           main] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 284 ms
2025-10-25T09:50:22.305-05:00  INFO 24611 --- [websocketintro] [           main] o.s.b.w.a.WelcomePageHandlerMapping      : Adding welcome page: class path resource [static/index.html]
2025-10-25T09:50:22.403-05:00  INFO 24611 --- [websocketintro] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2025-10-25T09:50:22.406-05:00  INFO 24611 --- [websocketintro] [           main] c.w.w.WebsocketintroApplication          : Started WebsocketintroApplication in 0.613 seconds (process running for 0.75)
```

5. Verifique que se esté ejecutando accediendo a:
 --- <http://localhost:8080/status>
 ![alt text](/readme/img/image1.png)

6. Verifique que el servidor esté entregando elementos estáticos web entrando a:
 --- <http://localhost:8080/index.html>
 ![alt text](/readme/img/image2.png)

## Construyamos el EndPoint el servidor con Websockets

 ```java
 package com.weobsocketintro.websocketintro.Controller;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
@Component
@ServerEndpoint("/timer")
public class TimerEndpoint {
    private static final Logger logger = Logger.getLogger("ETFEndpoint");
    
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    public static void send (String msg){
        try {
            for (Session s: queue){
                s.getBasicRemote().sendText(msg);
                logger.log(Level.INFO, "Sent: {0}",msg);
            }
        } catch (IOException e) {
            logger.log(Level.INFO ,e.toString());
        }
    }

    @OnOpen
        public void openConnection(Session session) {
        queue.add(session);
        logger.log(Level.INFO, "Connection opened.");
        try {
        session.getBasicRemote().sendText("Connection established.");
        } catch (IOException ex) {
        Logger.getLogger(TimerEndpoint.class.getName()).log(Level.SEVERE,
        null, ex);
        }
    }

    @OnClose
    public void closedConnection(Session session) {
        queue.remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }

    
}
```

## Construyamos una clase que emita mensajes desde el servidor

```java
package com.weobsocketintro.websocketintro.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TimedMessageBroker {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    private static final Logger logger = Logger.getLogger(TimedMessageBroker.class.getName());

    @Scheduled(fixedRate = 5000)
    public void broadcast(){
        logger.log(Level.INFO, "broadcasting message");
        TimerEndpoint.send("The current time is " + formatter.format(new Date()));
    }
}
```

## Ahora construyamos un componente que nos ayude a configurar el contenedor IoC

Es necesario construir esta clase porque el contenedor de Servlets en Spring, TOMCAT,
tiene deshabilitado por defecto la detección de componentes Endpoints. Así, no carga los
componentes si no se le indica explícitamente. Esto parece un error de diseño pero por el
momento esta es la situación.
La clase ServerEndpointExporter detecta beans de tipo ServerEndpointConfig y los registra
con con el motor standard de java de webSockets. También detecta beans anotados con
ServerEndpoint y los registra igualmente.

```java
