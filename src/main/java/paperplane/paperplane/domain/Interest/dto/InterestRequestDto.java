package paperplane.paperplane.domain.Interest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.group.Group;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class InterestRequestDto {

    @ApiModel(value = "관심사 등록")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create{

        @NotNull
        @ApiModelProperty(value = "관심사 키워드", required = true)
        private String keyword;

        public Interest toEntity(){
            return Interest.builder()
                    .keyword(this.keyword)
                    .build();
        }
    }
}
