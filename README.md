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
 --- http://localhost:8080/status
 ![alt text](/readme/img/image1.png)

 6. Verifique que el servidor esté entregando elementos estáticos web entrando a:
 --- http://localhost:8080/index.html
 ![alt text](/readme/img/image2.png)