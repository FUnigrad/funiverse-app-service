package com.unigrad.funiverseappservice.payload.request;

import com.unigrad.funiverseappservice.entity.academic.Season;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeasonOnboardRequest {

    private List<Season> season;
}