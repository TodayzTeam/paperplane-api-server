package paperplane.paperplane.domain.usergroup.dto;

import lombok.*;
import paperplane.paperplane.domain.user.dto.UserResponseDto;
import paperplane.paperplane.domain.usergroup.UserGroup;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupResponseDto {

   private Integer groupId;
   private String groupName;

   public static UserGroupResponseDto of(UserGroup userGroup){
      return UserGroupResponseDto.builder()
              .groupId(userGroup.getGroup().getId())
              .groupName(userGroup.getGroup().getName())
              .build();
   }

   public static List<UserGroupResponseDto> of(List<UserGroup> userGroups){
      return userGroups.stream().map(UserGroupResponseDto::of).collect(Collectors.toList());
   }
}
