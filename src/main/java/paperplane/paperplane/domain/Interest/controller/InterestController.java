package paperplane.paperplane.domain.Interest.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.Interest.dto.InterestRequestDto;
import paperplane.paperplane.domain.Interest.dto.InterestResponseDto;
import paperplane.paperplane.domain.Interest.service.InterestService;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Interest Controller"})
@Slf4j
@RestController
@RequestMapping("/interest")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;

    @ApiOperation("관심사 검색")
    @GetMapping("/search/{interest}")
    public ResponseEntity<List<InterestResponseDto.Info>> searchInterest(@PathVariable String interest) throws Exception {
        return ResponseEntity.ok(interestService.searchInterestByKeyword(interest));
    }

    @ApiOperation("관심사 추천")
    @GetMapping("/recommend")
    public ResponseEntity<List<InterestResponseDto.Info>> searchInterest() throws Exception {
        return ResponseEntity.ok(interestService.recommendInterest());
    }


    @ApiOperation("내 관심사 조회, 헤더에 userId 필요")
    @GetMapping("/myinterest")
    public ResponseEntity <List<InterestResponseDto.Info>> MyInterest() throws Exception {
        return ResponseEntity.ok(interestService.getMyInterest());
    }

}
