package com.skhu.mid_skhu.app.controller.event;

import com.skhu.mid_skhu.app.dto.event.responseDto.EventSearchResponseDto;
import com.skhu.mid_skhu.app.service.event.EventDisplayService;
import com.skhu.mid_skhu.app.service.event.EventSearchService;
import com.skhu.mid_skhu.app.service.event.RandomEventDisplayService;
import com.skhu.mid_skhu.global.common.dto.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "행사/이벤트", description = "행사/이벤트를 조회하는 API 그룹")
@RequestMapping("/api/v1/event")
public class EventDisplayController {

    private final EventDisplayService eventDisplayService;
    private final EventSearchService eventSearchService;
    private final RandomEventDisplayService randomEventDisplayService;

    @GetMapping("/display")
    @Operation(
            summary = "신청중/마감임박 이벤트 조회",
            description = "신청중 이벤트와 마감임박 이벤트를 각각 하나씩 조회하는 API입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "신청중/마감임박 이벤트 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "URL문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<EventSearchResponseDto>>> displayOneOngoingAndOneEndingEvent() {
        ApiResponseTemplate<List<EventSearchResponseDto>> dataList = eventDisplayService.displayOneOngoingAndOneEndingEvent();
        return ResponseEntity.status(dataList.getStatus()).body(dataList);
    }

    @GetMapping("/random")
    @Operation(
            summary = "랜덤 이벤트 조회",
            description = "랜덤으로 두 가지 이벤트를 조회하는 API입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "랜덤 이벤트 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "URL문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<EventSearchResponseDto>>> displayRandomEvents() {
        ApiResponseTemplate<List<EventSearchResponseDto>> dataList = randomEventDisplayService.displayRandomEvents();
        return ResponseEntity.status(dataList.getStatus()).body(dataList);
    }

    @GetMapping("/{title}")
    @Operation(
            summary = "이벤트 이름으로 조회",
            description = "이벤트 이름에 부분 일치하는 이벤트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이벤트 이름으로 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "URL문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<EventSearchResponseDto>>> findByPartialTitle(@PathVariable("title") String partialTitle) {
        ApiResponseTemplate<List<EventSearchResponseDto>> response = eventSearchService.findByPartialTitle(partialTitle);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}