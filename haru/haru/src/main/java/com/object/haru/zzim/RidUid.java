package com.object.haru.zzim;

import java.io.Serializable;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RidUid implements Serializable { //복합키 사용 Serializable
    private Long rid;
    private Long uid;
}
