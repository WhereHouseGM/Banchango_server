package com.banchango;

import com.banchango.common.functions.users.FindUserById;
import com.banchango.common.functions.warehouses.FindWarehouseById;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.insurances.InsurancesRepository;
import com.banchango.domain.mainitemtypes.MainItemTypesRepository;
import com.banchango.domain.securitycompanies.SecurityCompaniesRepository;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseConditionsRepository;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsageRepository;
import com.banchango.domain.warehouses.WarehouseRepository;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautionRepository;
import com.banchango.domain.withdraws.WithdrawRepository;
import com.banchango.factory.entity.EstimateEntityFactory;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.entity.WarehouseEntityFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class ApiIntegrationTest {

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected WarehouseRepository warehouseRepository;

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

    @Autowired
    protected MainItemTypesRepository mainItemTypesRepository;

    @Autowired
    protected InsurancesRepository insurancesRepository;

    @Autowired
    protected SecurityCompaniesRepository securityCompaniesRepository;

    @Autowired
    protected DeliveryTypesRepository deliveryTypesRepository;

    @Autowired
    protected WarehouseFacilityUsageRepository warehouseFacilityUsageRepository;

    @Autowired
    protected WarehouseConditionsRepository warehouseConditionsRepository;

    @Autowired
    protected WithdrawRepository withdrawRepository;

    @Autowired
    protected WarehouseUsageCautionRepository warehouseUsageCautionRepository;

    @Autowired
    protected FindWarehouseById findWarehouseById;

    @Autowired
    protected FindUserById findUserById;

    @Before
    public void setup() {
        usersRepository.deleteAll();
        warehouseRepository.deleteAll();
        estimatesRepository.deleteAll();
    }

    @After
    public void release() {
        usersRepository.deleteAll();
        warehouseRepository.deleteAll();
        estimatesRepository.deleteAll();
    }
}
