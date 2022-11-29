package paperplane.paperplane;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class home {
    @GetMapping("/")
    public String test1234(){
        return "test1234";
    }
}