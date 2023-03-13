package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;

import java.util.List;

public interface IGroupService extends IBaseService<Group,Long> {

    boolean isNameExist(String name, Group.Type type);

    int getNextNameOrderForClass(String name);
}