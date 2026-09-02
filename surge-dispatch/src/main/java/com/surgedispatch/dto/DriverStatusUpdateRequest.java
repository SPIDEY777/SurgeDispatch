package com.surgedispatch.dto;

import com.surgedispatch.entity.DriverStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DriverStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private DriverStatus status;

    public DriverStatusUpdateRequest(DriverStatus status) {
        this.status = status;
    }
}
