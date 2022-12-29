package paperplane.paperplane.domain.user.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import paperplane.paperplane.domain.user.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private final UserRepository userRepository;

    UserServiceTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}