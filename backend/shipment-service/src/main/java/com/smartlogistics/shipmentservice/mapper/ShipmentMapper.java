package com.smartlogistics.shipmentservice.mapper;

import com.smartlogistics.shipmentservice.dto.ShipmentDto;
import com.smartlogistics.shipmentservice.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    @Mapping(target = "status", expression = "java(shipment.getStatus().name())")
    ShipmentDto.Response toResponse(Shipment shipment);

    ShipmentDto.PickupResponse toPickupResponse(PickupDetails pickupDetails);

    ShipmentDto.DropResponse toDropResponse(DropDetails dropDetails);

    ShipmentDto.PricingResponse toPricingResponse(ShipmentPricing pricing);

    ShipmentDto.DimensionResponse toDimensionResponse(ShipmentDimension dimension);

    ShipmentDto.ItemResponse toItemResponse(ShipmentItem item);

    ShipmentDto.DocumentResponse toDocumentResponse(ShipmentDocument document);

    ShipmentDto.ImageResponse toImageResponse(ShipmentImage image);

    @Mapping(target = "oldStatus", expression = "java(history.getOldStatus().name())")
    @Mapping(target = "newStatus", expression = "java(history.getNewStatus().name())")
    ShipmentDto.StatusHistoryResponse toStatusHistoryResponse(ShipmentStatusHistory history);

    ShipmentDto.CategoryResponse toCategoryResponse(ShipmentCategory category);
}
