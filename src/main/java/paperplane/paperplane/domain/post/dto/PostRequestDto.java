package paperplane.paperplane.domain.post.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
        @ApiModelProperty(value = "편지 제목", required = true)
        private String title;

        @NotNull
        @ApiModelProperty(value = "편지 내용", required = true)
        private String content;

        @ApiModelProperty(value = "관심사 키워드")
        private String keyword;

        @NotNull
        @ApiModelProperty(value = "편지 색깔, 'RED','GREEN','BLUE','BLACK' 4색", required = true)
        private String Color;

        @ApiModelProperty(value = "그룹 코드")
        private String code;

        @ApiModelProperty(value = "회신 여부")
        private Boolean isReply;


    }

}
