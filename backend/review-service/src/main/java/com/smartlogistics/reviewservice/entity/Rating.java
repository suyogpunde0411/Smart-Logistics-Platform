package com.smartlogistics.reviewservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "ratings")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating extends BaseEntity {

    private Integer communication;
    private Integer punctuality;
    private Integer professionalism;
    private Integer vehicleCondition;
    private Integer cargoSafety;
    private Integer paymentExperience;
    private Integer overallExperience;
}
