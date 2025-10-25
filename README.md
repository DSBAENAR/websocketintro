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
