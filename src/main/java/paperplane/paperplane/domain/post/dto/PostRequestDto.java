package paperplane.paperplane.domain.post.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import paperplane.paperplane.domain.post.PostColor;

import javax.validation.constraints.NotNull;

public class PostRequestDto {

    @ApiModel(value = "편지 생성")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create{
        @NotNull
        @ApiModelProperty(value = "수신자그룹, 'RAND'-지원 , 'GROUP'- 미지원 ", required = true)
        private String receiveGroup;
        @NotNull
        @ApiModelProperty(value = "편지 제목", required = true)
        private String title;
        @NotNull
        @ApiModelProperty(value = "편지 내용", required = true)
        private String content;

        @ApiModelProperty(value = "관심사 키워드")
        private String keyword;

        @NotNull
        @ApiModelProperty(value = "편지 색깔, 'RED','GREEN','BLUE','BLACK' 4색", required = true)
        private String color;

        @ApiModelProperty(value = "그룹 코드")
        private String code;

        @ApiModelProperty(value = "회신하는 편지인지 여부")
        private Boolean isReply;
    }

    @ApiModel(value="post 검색")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {
        @ApiModelProperty(value = "검색어")
        private String query;
    }

}
