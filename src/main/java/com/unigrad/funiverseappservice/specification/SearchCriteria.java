package com.unigrad.funiverseappservice.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {

    private String field;

    private String operator;

    private String value;
}