package paperplane.paperplane.domain.group.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import paperplane.paperplane.domain.group.Group;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class GroupRequestDto {

    @ApiModel(value = "그룹 생성")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create{

        @NotNull
        @ApiModelProperty(value = "그룹 이름", required = true)
        private String name;

        public Group toEntity(){
            return Group.builder()
                    .name(this.name)
                    .code(UUID.randomUUID().toString())
                    .build();
        }
    }

    @ApiModel(value = "그룹 참가")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Join{

        @NotNull
        @ApiModelProperty(value = "그룹 코드", required = true)
        private String code;

    }

}
