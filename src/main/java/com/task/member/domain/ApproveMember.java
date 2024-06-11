package com.task.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * 승인 사용자 엔티티
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "approve_member")
public class ApproveMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Comment("이름")
    @Column(name = "name")
    private String name;

    @Comment("주민등록번호")
    @Column(name = "reg_no")
    private String regNo;
}
