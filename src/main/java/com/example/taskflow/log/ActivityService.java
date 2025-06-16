package com.example.taskflow.log;


import com.example.taskflow.log.dto.response.ActivityLogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

//    public ActivityLog save(ActivityLogResponseDto responseDto) {
//
//    }
}
