package com.smartlogistics.truckservice.mapper;

import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.truckservice.entity.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TruckMapper {

    TruckDTO.Response toResponse(Truck truck);

    TruckDTO.CapacityDto toCapacityDto(TruckCapacity capacity);

    TruckDTO.AvailabilityDto toAvailabilityDto(TruckAvailability availability);

    TruckDTO.LocationDto toLocationDto(TruckLocation location);

    TruckDTO.DocumentDto toDocumentDto(TruckDocument document);

    TruckDTO.InsuranceDto toInsuranceDto(TruckInsurance insurance);

    TruckDTO.MaintenanceDto toMaintenanceDto(TruckMaintenance maintenance);

    TruckDTO.ImageDto toImageDto(TruckImage image);

    TruckDTO.StatusHistoryDto toStatusHistoryDto(TruckStatusHistory history);
}
