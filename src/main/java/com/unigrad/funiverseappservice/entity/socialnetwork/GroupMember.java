package com.unigrad.funiverseappservice.entity.socialnetwork;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMember {

    @EmbeddedId
    private GroupMemberKey groupMemberKey;

    private boolean isGroupAdmin;

    @ManyToOne
    @MapsId("userId")
    private UserDetail user;

    @ManyToOne
    @MapsId("groupId")
    private Group group;

    @Embeddable
    @Setter
    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupMemberKey implements Serializable {

        private Long userId;

        private Long groupId;
    }
}