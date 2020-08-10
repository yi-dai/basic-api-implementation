package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RsEventDB {
    private String eventName;
    private String keyWord;
    private String userId;
}
