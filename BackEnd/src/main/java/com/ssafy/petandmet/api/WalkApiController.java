package com.ssafy.petandmet.api;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus;
import com.ssafy.petandmet.dto.walk.Result;
import com.ssafy.petandmet.dto.walk.SignWalkRequest;
import com.ssafy.petandmet.dto.walk.WalkAbleTime;
import com.ssafy.petandmet.service.WalkService;
import com.ssafy.petandmet.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/walk")
@Slf4j
public class WalkApiController {
    private final WalkService walkService;

    /**
     * 산책 가능 시간 조회
     *
     * @param map 쿼리 스트링
     * @return 시간별 산책 가능 여부
     */
    @GetMapping("/time")
    public Result getWalkAbleTime(@RequestParam Map<String, String> map) {
        List<WalkAbleTime> walkAbleTimes = walkService.getWalkTimes(map);
        Map<String, Object> result = new HashMap<>();
        result.put("message", "산책 등록 가능 시간 조회 성공");
        result.put("status", 200);
        result.put("walkTimes", walkAbleTimes);
        return new Result(true, result, "null");
    }


    /**
     * 산책 등록
     *
     * @param request 센터 uuid, 동물 uuid, 연월일, 시간
     * @return 산책 등록 결과
     */
    @PostMapping
    public Result signWalk(@RequestBody SignWalkRequest request) {
        log.debug(request.toString());
        Map<String, Object> result = new HashMap<>();
        try {
            walkService.signWalk(request);
            result.put("status", HttpStatus.SC_OK);
            result.put("message", "산책 등록 성공");
            return new Result(true, result, null);
        } catch (Exception e) {
            result.put("status", HttpStatus.SC_BAD_REQUEST);
            result.put("message", e.getMessage());
            return new Result(true, result, null);
        }
    }

    /**
     * USER가 신청한 산책 정보 불러오기
     *
     * @return 등록한 산책 정보
     */
    @GetMapping("/user")
    public Result getUserWalkTime() {
        Map<String, Object> result = new HashMap<>();
        Optional<String> userUuid = SecurityUtil.getCurrentUserUuid();
        if (userUuid.isEmpty()) {
            result.put("status", HttpStatus.SC_BAD_REQUEST);
            result.put("message", "사용자 정보가 존재하지 않습니다.");
            return new Result(false, null, result);
        }

        result.put("status", HttpStatus.SC_OK);
        result.put("message", "등록한 산책 불러오기 성공.");
        result.put("user_walk_times", walkService.getUserWalkTime(userUuid.get()));
        return new Result(true, result, null);
    }
}
