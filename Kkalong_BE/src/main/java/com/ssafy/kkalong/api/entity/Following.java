package com.ssafy.kkalong.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="FOLLOWING")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Following {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
}
