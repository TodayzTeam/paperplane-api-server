package paperplane.paperplane.domain.post.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paperplane.paperplane.domain.post.service.PostService;

@Api(tags = {"PostController"})
@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
}
