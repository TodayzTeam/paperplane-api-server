package paperplane.paperplane.domain.Interest.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paperplane.paperplane.domain.Interest.service.InterestService;

@Api(tags = {"Interest Controller"})
@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;
}
