package com.example.taskflow.Dashborad.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ProgressRatioDto {

    private double myProgressRate;
    private double teamProgressRate;
}
