package com.banchango;

import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.factory.entity.EstimateEntityFactory;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.entity.WarehouseEntityFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class ApiIntegrationTest {

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected WarehousesRepository warehousesRepository;

    @Autowired
    protected EstimatesRepository estimatesRepository;

    @Autowired
    protected UserEntityFactory userEntityFactory;

    @Autowired
    protected WarehouseEntityFactory warehouseEntityFactory;

    @Autowired
    protected EstimateEntityFactory estimateEntityFactory;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Before
    public void setup() {
        usersRepository.deleteAll();
        warehousesRepository.deleteAll();
        estimatesRepository.deleteAll();
    }

    @After
    public void release() {
        usersRepository.deleteAll();
        warehousesRepository.deleteAll();
        estimatesRepository.deleteAll();
    }
}
