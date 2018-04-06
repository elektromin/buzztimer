package se.elektromin.buzztimer.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("api/hello")
    public Hello hello(@RequestParam(value="name", defaultValue = "World") final String name) {
        final Hello hello = new Hello("Hello " + name + "!");
        return hello;
    }
}
