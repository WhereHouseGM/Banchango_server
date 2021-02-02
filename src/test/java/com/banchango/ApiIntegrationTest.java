package com.banchango;

import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.insurances.InsurancesRepository;
import com.banchango.domain.mainitemtypes.MainItemTypesRepository;
import com.banchango.domain.securitycompanies.SecurityCompaniesRepository;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseConditionsRepository;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsagesRepository;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.domain.withdraws.WithdrawsRepository;
import com.banchango.factory.entity.EstimateEntityFactory;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.entity.WarehouseEntityFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Autowired
    protected MainItemTypesRepository mainItemTypesRepository;

    @Autowired
    protected InsurancesRepository insurancesRepository;

    @Autowired
    protected SecurityCompaniesRepository securityCompaniesRepository;

    @Autowired
    protected DeliveryTypesRepository deliveryTypesRepository;

    @Autowired
    protected WarehouseFacilityUsagesRepository warehouseFacilityUsagesRepository;

    @Autowired
    protected WarehouseConditionsRepository warehouseConditionsRepository;

    @Autowired
    protected WithdrawsRepository withdrawsRepository;

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
