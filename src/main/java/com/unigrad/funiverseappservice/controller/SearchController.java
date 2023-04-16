package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Combo;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Major;
import com.unigrad.funiverseappservice.entity.academic.Specialization;
import com.unigrad.funiverseappservice.entity.academic.Subject;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.ComboDTO;
import com.unigrad.funiverseappservice.payload.DTO.PostDTO;
import com.unigrad.funiverseappservice.payload.response.SearchResultResponse;
import com.unigrad.funiverseappservice.service.IComboService;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.IGroupMemberService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IMajorService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.ISpecializationService;
import com.unigrad.funiverseappservice.service.ISubjectService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import com.unigrad.funiverseappservice.specification.SearchCriteria;
import com.unigrad.funiverseappservice.util.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {

    private final ISyllabusService syllabusService;

    private final ICurriculumService curriculumService;

    private final IUserDetailService userDetailService;

    private final IGroupService groupService;

    private final ISubjectService subjectService;

    private final IMajorService majorService;

    private final ISpecializationService specializationService;

    private final IComboService comboService;

    private final IPostService postService;

    private final IGroupMemberService groupMemberService;

    private final DTOConverter dtoConverter;

    @GetMapping
    public ResponseEntity<List<?>> search(@RequestParam String entity,
                                         @RequestParam String[] field,
                                         @RequestParam String[] operator,
                                         @RequestParam(defaultValue = "") String[] value){
        //in case the condition of search criteria is not enough, search base on the least criteria
        int noCriteria = Math.min(Math.min(field.length, operator.length), value.length);

        List<SearchCriteria> searchCriteria = new ArrayList<>();
        for (int i = 0; i < noCriteria; i++) {
            searchCriteria.add(new SearchCriteria(field[i], operator[i], value[i] == null ? "" : value[i]));
        }

        switch (entity){
            case "syllabus" -> {
                EntitySpecification<Syllabus> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(syllabusService.search(specification));
            }
            case "curriculum" -> {
                EntitySpecification<Curriculum> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(curriculumService.search(specification));
            }
            case "user" -> {
                EntitySpecification<UserDetail> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(userDetailService.search(specification));
            }
            case "group" -> {
                EntitySpecification<Group> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(groupService.search(specification));
            }
            case "subject" -> {
                EntitySpecification<Subject> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(subjectService.search(specification));
            }
            case "major" -> {
                EntitySpecification<Major> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(majorService.search(specification));
            }
            case "specialization" -> {
                EntitySpecification<Specialization> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(specializationService.search(specification));
            }
            case "combo" -> {
                EntitySpecification<Combo> specification = new EntitySpecification<>(searchCriteria);
                List<Combo> combos = comboService.search(specification);

                for (Combo combo : combos) {
                    //noinspection OptionalGetWithoutIsPresent
                    List<Syllabus> syllabi = combo.getSyllabi().stream()
                            .map(syllabus -> syllabusService.get(syllabus.getId()).get())
                            .collect(Collectors.toList());

                    combo.setSyllabi(syllabi);
                }

                return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(combos, ComboDTO[].class)).toList());
            }
            default -> throw new IllegalStateException("Unexpected value: " + entity);
        }
    }

    @GetMapping("workspace")
    public ResponseEntity<SearchResultResponse> searchWorkspace(@RequestParam String value) {
        List<SearchCriteria> searchUserCriteria = new ArrayList<>(List.of(
                new SearchCriteria("name", "like", value),
                new SearchCriteria("code", "like", value))
        );

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<UserDetail> userDetails = userDetailService.search(new EntitySpecification<>(searchUserCriteria));
        List<Group> groups = groupService.getAllForUser(userDetail.getId(), value);

        List<SearchResultResponse.GroupResult> groupResults = groups.stream()
                .map(group -> {
                    SearchResultResponse.GroupResult result = dtoConverter.convert(group, SearchResultResponse.GroupResult.class);
                    result.setNumOfMembers(groupMemberService.countMemberInGroup(result.getId()));
                    return result;
                }).toList();

        List<Post> posts = postService.getAllContainContentForUser(userDetail.getId(), value);

        return ResponseEntity.ok(SearchResultResponse.builder()
                .users(Arrays.stream(dtoConverter.convert(userDetails, SearchResultResponse.UserResult[].class)).toList())
                .groups(groupResults)
                .posts(Arrays.stream(dtoConverter.convert(posts, PostDTO[].class)).toList())
                .build());
    }

    @GetMapping("group/{id}")
    public ResponseEntity<List<PostDTO>> searchGroup(@RequestParam String value, @PathVariable Long id) {

        if (!groupService.isExist(id)) {
            return ResponseEntity.notFound().build();
        }

        List<Post> posts = postService.getAllInGroupWhoseContentContain(id, value);

        return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(posts, PostDTO[].class)).toList());
    }

    @GetMapping("chat")
    public ResponseEntity<SearchResultResponse> searchChat(@RequestParam String value) {
        List<SearchCriteria> searchUserCriteria = new ArrayList<>(List.of(
                new SearchCriteria("name", "like", value),
                new SearchCriteria("code", "like", value))
        );

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<UserDetail> userDetails = userDetailService.search(new EntitySpecification<>(searchUserCriteria));
        List<Group> groups = groupService.getAllForUser(userDetail.getId(), value);

        List<SearchResultResponse.GroupResult> groupResults = groups.stream()
                .map(group -> {
                    SearchResultResponse.GroupResult result = dtoConverter.convert(group, SearchResultResponse.GroupResult.class);
                    result.setNumOfMembers(groupMemberService.countMemberInGroup(result.getId()));
                    return result;
                }).toList();

        return ResponseEntity.ok(SearchResultResponse.builder()
                .users(Arrays.stream(dtoConverter.convert(userDetails, SearchResultResponse.UserResult[].class)).toList())
                .groups(groupResults)
                .posts(new ArrayList<>())
                .build());
    }
}