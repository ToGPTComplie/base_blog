package com.example.base_blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_username", columnNames = {"username"}),
        @UniqueConstraint(name = "uk_email", columnNames = {"email"}),
        @UniqueConstraint(name = "mobile", columnNames = {"mobile"})

})
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 64,message = "用户名长度必须在1到64个字符之间")
    @Column(name = "username", nullable = false, unique = true, length = 64)
    private String username;

    @Column(name = "password",  nullable = false, length = 255)
    private String password;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "mobile", length = 11)
    private String mobile;

    @Column(name = "nickname", length = 128)
    private String nickname;

    @Column(name = "status", nullable = false, length = 1)
    private Integer status = 1;

    @Column(name = "deleted", nullable = false, length = 1)
    private Boolean deleted = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",  nullable = false)
    private LocalDateTime updatedAt;
}
