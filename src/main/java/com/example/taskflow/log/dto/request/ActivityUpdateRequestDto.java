package com.example.taskflow.log.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ActivityUpdateRequestDto {

    @NotNull
    private String description;

}
