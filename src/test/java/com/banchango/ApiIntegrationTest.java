package com.banchango;

import com.banchango.common.functions.users.FindUserById;
import com.banchango.common.functions.warehouses.FindWarehouseById;
import com.banchango.domain.deliverytypes.DeliveryTypeRepository;
import com.banchango.domain.estimates.EstimateRepository;
import com.banchango.domain.insurances.InsuranceRepository;
import com.banchango.domain.mainitemtypes.MainItemTypeRepository;
import com.banchango.domain.securitycompanies.SecurityCompanyRepository;
import com.banchango.domain.users.UserRepository;
import com.banchango.domain.warehouseconditions.WarehouseConditionRepository;
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
    protected UserRepository userRepository;

    @Autowired
    protected WarehouseRepository warehouseRepository;

    @Autowired
    protected EstimateRepository estimateRepository;

    @Autowired
    protected UserEntityFactory userEntityFactory;

    @Autowired
    protected WarehouseEntityFactory warehouseEntityFactory;

    @Autowired
    protected EstimateEntityFactory estimateEntityFactory;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected MainItemTypeRepository mainItemTypeRepository;

    @Autowired
    protected InsuranceRepository insuranceRepository;

    @Autowired
    protected SecurityCompanyRepository securityCompanyRepository;

    @Autowired
    protected DeliveryTypeRepository deliveryTypeRepository;

    @Autowired
    protected WarehouseFacilityUsageRepository warehouseFacilityUsageRepository;

    @Autowired
    protected WarehouseConditionRepository warehouseConditionRepository;

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
        userRepository.deleteAll();
        warehouseRepository.deleteAll();
        estimateRepository.deleteAll();
    }

    @After
    public void release() {
        userRepository.deleteAll();
        warehouseRepository.deleteAll();
        estimateRepository.deleteAll();
    }
}
