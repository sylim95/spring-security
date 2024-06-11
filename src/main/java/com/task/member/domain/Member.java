package com.task.member.domain;

import com.task.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @Comment("사용자 아이디")
    @Column(name = "user_id")
    private String userId;

    @Comment("비밀번호")
    @Column(name = "password")
    private String password;

    @Comment("사용자 이름")
    @Column(name = "name")
    private String name;

    @Comment("사용자 주민등록번호")
    @Column(name = "reg_no")
    private String regNo;
}
