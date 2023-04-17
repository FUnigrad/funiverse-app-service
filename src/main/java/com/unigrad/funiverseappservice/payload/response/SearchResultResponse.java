package com.unigrad.funiverseappservice.payload.response;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.Role;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.EntityBaseDTO;
import com.unigrad.funiverseappservice.payload.DTO.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResultResponse {

    private List<GroupResult> groups;

    private List<UserResult> users;

    private List<PostDTO> posts;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResult extends EntityBaseDTO {
        private Role role;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupResult extends EntityBaseDTO {
        private int numOfMembers;

        private Group.Type type;

        private List<UserDetail> members;
    }
}