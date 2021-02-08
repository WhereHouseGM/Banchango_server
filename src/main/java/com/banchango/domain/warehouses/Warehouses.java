package com.banchango.domain.warehouses;

import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.domain.BaseTimeEntity;
import com.banchango.domain.deliverytypes.DeliveryType;
import com.banchango.domain.insurances.Insurance;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.securitycompanies.SecurityCompany;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsage;
import com.banchango.domain.warehouseimages.WarehouseImages;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.warehouses.dto.WarehouseUpdateRequestParentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(length = 1000)
    private String blogUrl;

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<DeliveryType> deliveryTypes = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<Insurance> insurances = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<SecurityCompany> securityCompanies = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<WarehouseCondition> warehouseConditions = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<WarehouseFacilityUsage> warehouseFacilityUsages = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<WarehouseUsageCautions> warehouseUsageCautions = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseImages> warehouseImages = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<MainItemType> mainItemTypes = new ArrayList<>();

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
        if(dto instanceof WarehouseAdminUpdateRequestDto) {
            WarehouseAdminUpdateRequestDto _dto = (WarehouseAdminUpdateRequestDto)dto;
            this.status = _dto.getStatus();
            this.blogUrl = _dto.getBlogUrl();
        }
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
    }

    public void updateStatus(WarehouseStatus status) {
        this.status = status;
    }

}