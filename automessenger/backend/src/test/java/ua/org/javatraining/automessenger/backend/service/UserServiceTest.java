package ua.org.javatraining.automessenger.backend.service;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.org.javatraining.automessenger.backend.config.TestDataBaseConfig;
import ua.org.javatraining.automessenger.backend.repository.UserRepository;

import javax.annotation.Resource;

/**
 * Created by fisher on 28.07.15.
 */

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataBaseConfig.class)
@WebAppConfiguration
public class UserServiceTest {

//    @Resource
//    private EntityManagerFactory emf;
//    protected EntityManager em;

    @Resource
    private UserRepository userService;

    @Before
    public void setUp() throws Exception {
//        em = emf.createEntityManager();
    }

    @Test
    public void testSaveUser() throws Exception {
        //userService.addUser(UserUtil.createUser());
    }
}