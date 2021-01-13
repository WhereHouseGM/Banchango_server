package com.banchango.domain.warehouses;

import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.domain.BaseTimeEntity;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.securitycompanies.SecurityCompanies;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehouseimages.WarehouseImages;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.warehouses.dto.WarehouseUpdateRequestDto;
import com.banchango.warehouses.dto.WarehouseUpdateRequestParentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Entity
public class Warehouses extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Integer id;

    @Column
    private Integer userId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer space;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String addressDetail;

    @Column(length = 400, nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer availableWeekdays;

    @Column(nullable = false, length = 40)
    private String openAt;

    @Column(nullable = false, length = 40)
    private String closeAt;

    @Column(length = 100)
    private String availableTimeDetail;

    @Column(nullable = false)
    private Boolean cctvExist;

    @Column(nullable = false)
    private Boolean doorLockExist;

    @Column(nullable = false, columnDefinition = "ENUM('HEATING', 'COOLING', 'BOTH', 'NONE')")
    @Enumerated(EnumType.STRING)
    private AirConditioningType airConditioningType;

    @Column(nullable = false)
    private Boolean workerExist;

    @Column(nullable = false)
    private Boolean canPark;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WarehouseType warehouseType;

    @Column(nullable = false)
    private Integer minReleasePerMonth;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WarehouseStatus status;

    @Column(length = 300)
    private String blogUrl;

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private List<DeliveryTypes> deliveryTypes = new ArrayList<>();

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private List<Insurances> insurances = new ArrayList<>();

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private List<SecurityCompanies> securityCompanies = new ArrayList<>();

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private List<WarehouseConditions> warehouseConditions = new ArrayList<>();

    @Setter
    @OneToMany( cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private List<WarehouseFacilityUsages> warehouseFacilityUsages = new ArrayList<>();

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private List<WarehouseUsageCautions> warehouseUsageCautions = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<WarehouseImages> warehouseImages = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<MainItemTypes> mainItemTypes = new ArrayList<>();

    @Builder
    public Warehouses(String name, Integer space, String address, String addressDetail, String description, Integer availableWeekdays, String openAt, String closeAt, String availableTimeDetail, Boolean cctvExist, Boolean doorLockExist, AirConditioningType airConditioningType, Boolean workerExist, Boolean canPark, Integer userId, Double latitude, Double longitude, WarehouseType warehouseType, Integer minReleasePerMonth, WarehouseStatus status, String blogUrl) {
        this.name = name;
        this.space = space;
        this.address = address;
        this.addressDetail = addressDetail;
        this.description = description;
        this.availableWeekdays = availableWeekdays;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.availableTimeDetail = availableTimeDetail;
        this.cctvExist = cctvExist;
        this.doorLockExist = doorLockExist;
        this.airConditioningType = airConditioningType;
        this.workerExist = workerExist;
        this.canPark = canPark;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.warehouseType = warehouseType;
        this.minReleasePerMonth = minReleasePerMonth;
        this.status = status;
        this.blogUrl = blogUrl;
    }

    public WarehouseImages getMainImage() {
        for(WarehouseImages image : warehouseImages) {
            if(image.isMain()) return image;
        }
        return null;
    }

    public boolean isViewable() {
        return this.status.equals(WarehouseStatus.VIEWABLE);
    }

    public void update(WarehouseUpdateRequestParentDto dto) {
        this.name = dto.getName();
        this.space = dto.getSpace();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.description = dto.getDescription();
        this.availableWeekdays = dto.getAvailableWeekdays();
        this.openAt = dto.getOpenAt();
        this.closeAt = dto.getCloseAt();
        this.availableTimeDetail = dto.getAvailableTimeDetail();
        this.cctvExist = dto.getCctvExist();
        this.doorLockExist = dto.getDoorLockExist();
        this.airConditioningType = dto.getAirConditioningType();
        this.workerExist = dto.getWorkerExist();
        this.canPark = dto.getCanPark();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.warehouseType = dto.getWarehouseType();
        this.minReleasePerMonth = dto.getMinReleasePerMonth();
        this.deliveryTypes = dto.getDeliveryTypes().stream()
                .map(type -> new DeliveryTypes(type, this)).collect(Collectors.toList());
        this.insurances = dto.getInsurances().stream()
                .map(insurance -> new Insurances(insurance, this)).collect(Collectors.toList());
        this.securityCompanies = dto.getSecurityCompanies().stream()
                .map(company -> new SecurityCompanies(company, this)).collect(Collectors.toList());
        this.warehouseConditions = dto.getWarehouseCondition().stream()
                .map(condition -> new WarehouseConditions(condition, this)).collect(Collectors.toList());
        if(dto.getWarehouseFacilityUsages() != null) {
            this.warehouseFacilityUsages = dto.getWarehouseFacilityUsages().stream()
                    .map(usage -> new WarehouseFacilityUsages(usage, this)).collect(Collectors.toList());
        }
        if(dto.getWarehouseUsageCautions() != null) {
            this.warehouseUsageCautions = dto.getWarehouseUsageCautions().stream()
                    .map(caution -> new WarehouseUsageCautions(caution, this)).collect(Collectors.toList());
        }
        this.mainItemTypes = dto.getMainItemTypes().stream()
                .map(type -> new MainItemTypes(type, this)).collect(Collectors.toList());
        if(dto instanceof WarehouseAdminUpdateRequestDto) {
            this.status = ((WarehouseAdminUpdateRequestDto) dto).getStatus();
        }

    }

    public void updateStatus(WarehouseStatus status) {
        this.status = status;
    }

}