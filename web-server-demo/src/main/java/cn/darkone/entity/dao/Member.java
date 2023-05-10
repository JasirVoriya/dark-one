package cn.darkone.entity.dao;

import cn.darkone.entity.enums.Color;
import cn.darkone.entity.enums.Sex;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private String account;
    private String password;
    private Integer code;
    private String[] email;
    private Sex[] sex;
    private Color[] color;
}
