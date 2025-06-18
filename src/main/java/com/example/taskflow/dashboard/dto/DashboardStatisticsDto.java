package com.example.taskflow.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class DashboardStatisticsDto {

//    private Long totalTasks; // 테스크 전체갯수
//
//
//
//    private double inProgressTasks; // 전체 진행상황
//
//    private Long todoTasks; // todo테스크
//
//    private Long overdueTasks; // 기한초가된 테스크
//
//    private Long teamProgress; // 팀진행상황
//
//    private Long myTasksToday; // 오늘 내가 할일
//
//    private Long completedTasks; // 완성된 테스크 갯수
//    private double completionRate; // 완료율



    private long totalCount;         // 전체 태스크 수
    private long doneCount;          // DONE 태스크 수
    private double weeklyChangeRate; // 전체 작업 수 증가율 (%)

    private long inProgressCount;    // IN_PROGRESS 태스크 수
    private double inProgressRate;   // 진행률 (%)

    private long todoCount;          // TODO 태스크 수
    private double completionRate;   // 완료율 (%)

    private long overdueCount;       // 기한 초과 태스크 수
    private double overdueRate;      // 지연율 (%)
}
