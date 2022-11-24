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

    @ApiOperation("관심사 db에 생성")
    @GetMapping("/create")
    public ResponseEntity<InterestResponseDto.Info> createInterest(@Valid InterestRequestDto.Create create) throws Exception {
        return ResponseEntity.ok(InterestResponseDto.Info.builder()
                .id(1)
                .keyword(create.getKeyword())
                .build());
    }

    @ApiOperation("관심사 db에서  삭제")
    @DeleteMapping("/delete/{interest}")
    public ResponseEntity<Void> deleteInterest(@PathVariable final String interest) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("관심사 검색")
    @GetMapping("/search/{interest}")
    public ResponseEntity<List<InterestResponseDto.Info>> searchInterest(@PathVariable final String interest) throws Exception {
        List<InterestResponseDto.Info> interestResponseDtoList=new ArrayList<>();
        interestResponseDtoList.add(InterestResponseDto.Info.builder()
                .id(1)
                .keyword("관심사1")
                .build());
        interestResponseDtoList.add(InterestResponseDto.Info.builder()
                .id(2)
                .keyword("관심사2")
                .build());
        return ResponseEntity.ok(interestResponseDtoList);
    }

    @ApiOperation("관심사 추천")
    @GetMapping("/recommend")
    public ResponseEntity<List<InterestResponseDto.Info>> searchInterest() throws Exception {
        List<InterestResponseDto.Info> interestResponseDtoList=new ArrayList<>();
        interestResponseDtoList.add(InterestResponseDto.Info.builder()
                .id(1)
                .keyword("관심사1")
                .build());
        interestResponseDtoList.add(InterestResponseDto.Info.builder()
                .id(2)
                .keyword("관심사2")
                .build());
        return ResponseEntity.ok(interestResponseDtoList);
    }


    @ApiOperation("내 관심사 조회")
    @GetMapping("/group/{userid}")
    public ResponseEntity <List<InterestResponseDto.Info>> getMyInterest(@PathVariable Integer userid) throws Exception {
        List<InterestResponseDto.Info> infoList= new ArrayList<>();
        infoList.add(InterestResponseDto.Info.of(Interest.builder()
                .id(1)
                .keyword("해변")
                .build()));
        infoList.add(InterestResponseDto.Info.of(Interest.builder()
                .id(2)
                .keyword("바닷가")
                .build()));
        return ResponseEntity.ok(infoList);
    }

}
