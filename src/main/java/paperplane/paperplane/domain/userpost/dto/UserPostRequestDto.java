package paperplane.paperplane.domain.userpost.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import paperplane.paperplane.domain.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;


@ApiModel(value = "편지 옵션")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostRequestDto {
        @NotNull
        @ApiModelProperty(value = "post id list", required = true)
        private List<Integer> postIdList;

}
