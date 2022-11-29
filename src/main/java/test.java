import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class test {
    @GetMapping("/")
    public String test1234(){
        return "test1234";
    }
}
