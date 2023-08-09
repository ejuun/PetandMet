package com.ssafy.petandmet.api;

import com.ssafy.petandmet.domain.Live;
import com.ssafy.petandmet.dto.animal.AnimalResponse;
import com.ssafy.petandmet.dto.animal.Result;
import com.ssafy.petandmet.dto.live.*;
import com.ssafy.petandmet.service.LiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class LiveApiController {

    private final LiveService liveService;

    //라이브 등록
    @PostMapping("api/v1/live")
    public Result createLive(@RequestBody CreateLiveRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            liveService.createLive(request);
            response.put("status", 200);
            response.put("message", "라이브 등록 성공");
            return new Result(true, response, "null");
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "라이브 등록 실패");
            return new Result(false, response, "null");
        }
    }

    //라이브 삭제
    @DeleteMapping("api/v1/live")
    public Result deleteLive(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            liveService.deleteLive(id);
            response.put("status", 200);
            response.put("message", "라이브 삭제 성공");
            return new Result(true, response, "null");
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "라이브 삭제 실패");
            return new Result(false, response, "null");
        }
    }

    //라이브 수정
    @PatchMapping("api/v1/live")
    public Result updateLive(@RequestBody UpdateLiveRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            liveService.updateLive(request);
            response.put("status", 200);
            response.put("message", "라이브 수정 성공");
            return new Result(true, response, "null");
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "라이브 수정 실패");
            return new Result(false, response,"null");
        }
    }

    //라이브 전체조회
    @GetMapping("api/v1/live")
    public Result getLiveList(@PageableDefault(size = 10) Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try{
            Page<Live> liveList = liveService.findLiveList(pageable);
            List<LiveResponseDto> live = liveList.stream()
                    .map(o -> new LiveResponseDto(o))
                    .collect(toList());

            response.put("status", 200);
            response.put("message", "라이브 전체조회 성공");
            response.put("total", live.stream().count());
            response.put("lives",live);
            return new Result(true, response, "null");
        }catch (Exception e){
            response.put("status", 500);
            response.put("message", "라이브 전체조회 실패");
            return new Result(false, response,"null");
        }
    }

    //라이브 필터링 조회
    @GetMapping("api/v1/live/search")
    public Result getLiveListByCenterUuid(@RequestParam String uuid) {
        Map<String, Object> response = new HashMap<>();
        try{
            List<Live> liveList = liveService.findLiveListByCenter(uuid);
            List<LiveListResponse> live = liveList.stream()
                    .map(o -> new LiveListResponse(o))
                    .collect(toList());
            response.put("status", 200);
            response.put("message", "라이브 필터링 조회 성공");
            response.put("total", live.stream().count());
            response.put("lives",live);
            return new Result(true, response, "null");
        }catch (Exception e){
            response.put("status", 500);
            response.put("message", "라이브 필터링 조회 실패");
            return new Result(false, response,"null");
        }
    }

    //라이브 상세조회
    @GetMapping("api/v1/live/detail")
    public Result getLiveDetail(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", 200);
            response.put("message", "라이브 상세조회 성공");
            response.put("board", liveService.findLiveDetail(id));

            return new Result(true, response, "null");
        }catch (Exception e){
            response.put("status", 500);
            response.put("message", "라이브 상세조회 조회 실패");
            return new Result(false, response,"null");
        }
    }

}
