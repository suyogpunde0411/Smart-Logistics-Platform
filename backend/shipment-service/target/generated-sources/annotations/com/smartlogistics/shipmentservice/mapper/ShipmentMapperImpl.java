package com.smartlogistics.shipmentservice.mapper;

import com.smartlogistics.shipmentservice.dto.ShipmentDto;
import com.smartlogistics.shipmentservice.entity.DropDetails;
import com.smartlogistics.shipmentservice.entity.PickupDetails;
import com.smartlogistics.shipmentservice.entity.Shipment;
import com.smartlogistics.shipmentservice.entity.ShipmentCategory;
import com.smartlogistics.shipmentservice.entity.ShipmentDimension;
import com.smartlogistics.shipmentservice.entity.ShipmentDocument;
import com.smartlogistics.shipmentservice.entity.ShipmentImage;
import com.smartlogistics.shipmentservice.entity.ShipmentItem;
import com.smartlogistics.shipmentservice.entity.ShipmentPricing;
import com.smartlogistics.shipmentservice.entity.ShipmentStatusHistory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T09:46:17+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class ShipmentMapperImpl implements ShipmentMapper {

    @Override
    public ShipmentDto.Response toResponse(Shipment shipment) {
        if ( shipment == null ) {
            return null;
        }

        UUID id = null;
        String trackingNumber = null;
        UUID businessOwnerId = null;
        String originAddress = null;
        Double originLatitude = null;
        Double originLongitude = null;
        String destinationAddress = null;
        Double destinationLatitude = null;
        Double destinationLongitude = null;
        String cargoType = null;
        Double totalWeight = null;
        Double totalVolume = null;
        String weightUnit = null;
        String volumeUnit = null;
        String description = null;
        Double budgetAmount = null;
        String budgetCurrency = null;
        String requiredTruckType = null;
        LocalDateTime expiresAt = null;
        ShipmentDto.PickupResponse pickupDetails = null;
        ShipmentDto.DropResponse dropDetails = null;
        ShipmentDto.PricingResponse pricing = null;
        ShipmentDto.DimensionResponse dimension = null;
        List<ShipmentDto.ItemResponse> items = null;
        List<ShipmentDto.DocumentResponse> documents = null;
        List<ShipmentDto.ImageResponse> images = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = shipment.getId();
        trackingNumber = shipment.getTrackingNumber();
        businessOwnerId = shipment.getBusinessOwnerId();
        originAddress = shipment.getOriginAddress();
        originLatitude = shipment.getOriginLatitude();
        originLongitude = shipment.getOriginLongitude();
        destinationAddress = shipment.getDestinationAddress();
        destinationLatitude = shipment.getDestinationLatitude();
        destinationLongitude = shipment.getDestinationLongitude();
        cargoType = shipment.getCargoType();
        totalWeight = shipment.getTotalWeight();
        totalVolume = shipment.getTotalVolume();
        weightUnit = shipment.getWeightUnit();
        volumeUnit = shipment.getVolumeUnit();
        description = shipment.getDescription();
        budgetAmount = shipment.getBudgetAmount();
        budgetCurrency = shipment.getBudgetCurrency();
        requiredTruckType = shipment.getRequiredTruckType();
        expiresAt = shipment.getExpiresAt();
        pickupDetails = toPickupResponse( shipment.getPickupDetails() );
        dropDetails = toDropResponse( shipment.getDropDetails() );
        pricing = toPricingResponse( shipment.getPricing() );
        dimension = toDimensionResponse( shipment.getDimension() );
        items = shipmentItemListToItemResponseList( shipment.getItems() );
        documents = shipmentDocumentListToDocumentResponseList( shipment.getDocuments() );
        images = shipmentImageListToImageResponseList( shipment.getImages() );
        createdAt = shipment.getCreatedAt();
        updatedAt = shipment.getUpdatedAt();

        String status = shipment.getStatus().name();

        ShipmentDto.Response response = new ShipmentDto.Response( id, trackingNumber, businessOwnerId, originAddress, originLatitude, originLongitude, destinationAddress, destinationLatitude, destinationLongitude, status, cargoType, totalWeight, totalVolume, weightUnit, volumeUnit, description, budgetAmount, budgetCurrency, requiredTruckType, expiresAt, pickupDetails, dropDetails, pricing, dimension, items, documents, images, createdAt, updatedAt );

        return response;
    }

    @Override
    public ShipmentDto.PickupResponse toPickupResponse(PickupDetails pickupDetails) {
        if ( pickupDetails == null ) {
            return null;
        }

        UUID id = null;
        String address = null;
        Double latitude = null;
        Double longitude = null;
        String city = null;
        String state = null;
        String pinCode = null;
        String contactName = null;
        String contactPhone = null;
        LocalDateTime scheduledAt = null;
        LocalDateTime completedAt = null;
        String specialInstructions = null;

        id = pickupDetails.getId();
        address = pickupDetails.getAddress();
        latitude = pickupDetails.getLatitude();
        longitude = pickupDetails.getLongitude();
        city = pickupDetails.getCity();
        state = pickupDetails.getState();
        pinCode = pickupDetails.getPinCode();
        contactName = pickupDetails.getContactName();
        contactPhone = pickupDetails.getContactPhone();
        scheduledAt = pickupDetails.getScheduledAt();
        completedAt = pickupDetails.getCompletedAt();
        specialInstructions = pickupDetails.getSpecialInstructions();

        ShipmentDto.PickupResponse pickupResponse = new ShipmentDto.PickupResponse( id, address, latitude, longitude, city, state, pinCode, contactName, contactPhone, scheduledAt, completedAt, specialInstructions );

        return pickupResponse;
    }

    @Override
    public ShipmentDto.DropResponse toDropResponse(DropDetails dropDetails) {
        if ( dropDetails == null ) {
            return null;
        }

        UUID id = null;
        String address = null;
        Double latitude = null;
        Double longitude = null;
        String city = null;
        String state = null;
        String pinCode = null;
        String contactName = null;
        String contactPhone = null;
        LocalDateTime scheduledAt = null;
        LocalDateTime completedAt = null;
        String specialInstructions = null;

        id = dropDetails.getId();
        address = dropDetails.getAddress();
        latitude = dropDetails.getLatitude();
        longitude = dropDetails.getLongitude();
        city = dropDetails.getCity();
        state = dropDetails.getState();
        pinCode = dropDetails.getPinCode();
        contactName = dropDetails.getContactName();
        contactPhone = dropDetails.getContactPhone();
        scheduledAt = dropDetails.getScheduledAt();
        completedAt = dropDetails.getCompletedAt();
        specialInstructions = dropDetails.getSpecialInstructions();

        ShipmentDto.DropResponse dropResponse = new ShipmentDto.DropResponse( id, address, latitude, longitude, city, state, pinCode, contactName, contactPhone, scheduledAt, completedAt, specialInstructions );

        return dropResponse;
    }

    @Override
    public ShipmentDto.PricingResponse toPricingResponse(ShipmentPricing pricing) {
        if ( pricing == null ) {
            return null;
        }

        UUID id = null;
        Double baseRate = null;
        Double distanceCharge = null;
        Double weightCharge = null;
        Double insuranceCharge = null;
        Double taxAmount = null;
        Double totalAmount = null;
        String currency = null;
        String pricingStatus = null;

        id = pricing.getId();
        baseRate = pricing.getBaseRate();
        distanceCharge = pricing.getDistanceCharge();
        weightCharge = pricing.getWeightCharge();
        insuranceCharge = pricing.getInsuranceCharge();
        taxAmount = pricing.getTaxAmount();
        totalAmount = pricing.getTotalAmount();
        currency = pricing.getCurrency();
        pricingStatus = pricing.getPricingStatus();

        ShipmentDto.PricingResponse pricingResponse = new ShipmentDto.PricingResponse( id, baseRate, distanceCharge, weightCharge, insuranceCharge, taxAmount, totalAmount, currency, pricingStatus );

        return pricingResponse;
    }

    @Override
    public ShipmentDto.DimensionResponse toDimensionResponse(ShipmentDimension dimension) {
        if ( dimension == null ) {
            return null;
        }

        UUID id = null;
        Double lengthCm = null;
        Double widthCm = null;
        Double heightCm = null;
        Double volumeCbm = null;
        String dimensionUnit = null;

        id = dimension.getId();
        lengthCm = dimension.getLengthCm();
        widthCm = dimension.getWidthCm();
        heightCm = dimension.getHeightCm();
        volumeCbm = dimension.getVolumeCbm();
        dimensionUnit = dimension.getDimensionUnit();

        ShipmentDto.DimensionResponse dimensionResponse = new ShipmentDto.DimensionResponse( id, lengthCm, widthCm, heightCm, volumeCbm, dimensionUnit );

        return dimensionResponse;
    }

    @Override
    public ShipmentDto.ItemResponse toItemResponse(ShipmentItem item) {
        if ( item == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        Integer quantity = null;
        Double weight = null;
        Double volume = null;
        String unit = null;
        String description = null;
        Double value = null;
        String currency = null;

        id = item.getId();
        name = item.getName();
        quantity = item.getQuantity();
        weight = item.getWeight();
        volume = item.getVolume();
        unit = item.getUnit();
        description = item.getDescription();
        value = item.getValue();
        currency = item.getCurrency();

        ShipmentDto.ItemResponse itemResponse = new ShipmentDto.ItemResponse( id, name, quantity, weight, volume, unit, description, value, currency );

        return itemResponse;
    }

    @Override
    public ShipmentDto.DocumentResponse toDocumentResponse(ShipmentDocument document) {
        if ( document == null ) {
            return null;
        }

        UUID id = null;
        String documentType = null;
        String documentNumber = null;
        String fileUrl = null;
        String fileName = null;
        String contentType = null;
        Long fileSizeBytes = null;
        LocalDate expiryDate = null;
        String status = null;
        LocalDateTime createdAt = null;

        id = document.getId();
        documentType = document.getDocumentType();
        documentNumber = document.getDocumentNumber();
        fileUrl = document.getFileUrl();
        fileName = document.getFileName();
        contentType = document.getContentType();
        fileSizeBytes = document.getFileSizeBytes();
        expiryDate = document.getExpiryDate();
        status = document.getStatus();
        createdAt = document.getCreatedAt();

        ShipmentDto.DocumentResponse documentResponse = new ShipmentDto.DocumentResponse( id, documentType, documentNumber, fileUrl, fileName, contentType, fileSizeBytes, expiryDate, status, createdAt );

        return documentResponse;
    }

    @Override
    public ShipmentDto.ImageResponse toImageResponse(ShipmentImage image) {
        if ( image == null ) {
            return null;
        }

        UUID id = null;
        String fileUrl = null;
        String fileName = null;
        String contentType = null;
        Long fileSizeBytes = null;
        String caption = null;
        LocalDateTime createdAt = null;

        id = image.getId();
        fileUrl = image.getFileUrl();
        fileName = image.getFileName();
        contentType = image.getContentType();
        fileSizeBytes = image.getFileSizeBytes();
        caption = image.getCaption();
        createdAt = image.getCreatedAt();

        ShipmentDto.ImageResponse imageResponse = new ShipmentDto.ImageResponse( id, fileUrl, fileName, contentType, fileSizeBytes, caption, createdAt );

        return imageResponse;
    }

    @Override
    public ShipmentDto.StatusHistoryResponse toStatusHistoryResponse(ShipmentStatusHistory history) {
        if ( history == null ) {
            return null;
        }

        UUID id = null;
        LocalDateTime changedAt = null;
        UUID changedBy = null;
        String remarks = null;

        id = history.getId();
        changedAt = history.getChangedAt();
        changedBy = history.getChangedBy();
        remarks = history.getRemarks();

        String oldStatus = history.getOldStatus().name();
        String newStatus = history.getNewStatus().name();

        ShipmentDto.StatusHistoryResponse statusHistoryResponse = new ShipmentDto.StatusHistoryResponse( id, oldStatus, newStatus, changedAt, changedBy, remarks );

        return statusHistoryResponse;
    }

    @Override
    public ShipmentDto.CategoryResponse toCategoryResponse(ShipmentCategory category) {
        if ( category == null ) {
            return null;
        }

        UUID id = null;
        String code = null;
        String displayName = null;
        String description = null;
        boolean active = false;
        Boolean requiresSpecialHandling = null;
        Boolean requiresRefrigeration = null;
        Boolean isHazardous = null;

        id = category.getId();
        code = category.getCode();
        displayName = category.getDisplayName();
        description = category.getDescription();
        active = category.isActive();
        requiresSpecialHandling = category.getRequiresSpecialHandling();
        requiresRefrigeration = category.getRequiresRefrigeration();
        isHazardous = category.getIsHazardous();

        ShipmentDto.CategoryResponse categoryResponse = new ShipmentDto.CategoryResponse( id, code, displayName, description, active, requiresSpecialHandling, requiresRefrigeration, isHazardous );

        return categoryResponse;
    }

    protected List<ShipmentDto.ItemResponse> shipmentItemListToItemResponseList(List<ShipmentItem> list) {
        if ( list == null ) {
            return null;
        }

        List<ShipmentDto.ItemResponse> list1 = new ArrayList<ShipmentDto.ItemResponse>( list.size() );
        for ( ShipmentItem shipmentItem : list ) {
            list1.add( toItemResponse( shipmentItem ) );
        }

        return list1;
    }

    protected List<ShipmentDto.DocumentResponse> shipmentDocumentListToDocumentResponseList(List<ShipmentDocument> list) {
        if ( list == null ) {
            return null;
        }

        List<ShipmentDto.DocumentResponse> list1 = new ArrayList<ShipmentDto.DocumentResponse>( list.size() );
        for ( ShipmentDocument shipmentDocument : list ) {
            list1.add( toDocumentResponse( shipmentDocument ) );
        }

        return list1;
    }

    protected List<ShipmentDto.ImageResponse> shipmentImageListToImageResponseList(List<ShipmentImage> list) {
        if ( list == null ) {
            return null;
        }

        List<ShipmentDto.ImageResponse> list1 = new ArrayList<ShipmentDto.ImageResponse>( list.size() );
        for ( ShipmentImage shipmentImage : list ) {
            list1.add( toImageResponse( shipmentImage ) );
        }

        return list1;
    }
}
