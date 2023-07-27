package com.ssafy.petandmet.service;

import com.ssafy.petandmet.domain.Center;
import com.ssafy.petandmet.domain.RoleType;
import com.ssafy.petandmet.domain.User;
import com.ssafy.petandmet.dto.jwt.Token;
import com.ssafy.petandmet.dto.user.CreateUserRequest;
import com.ssafy.petandmet.dto.user.LoginUserRequest;
import com.ssafy.petandmet.repository.CenterRepository;
import com.ssafy.petandmet.repository.UserRepository;
import com.ssafy.petandmet.util.JwtAuthenticationUtil;
import com.ssafy.petandmet.util.PasswordEncryptUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CenterRepository centerRepository;
    private final JwtAuthenticationUtil jwtAuthenticationUtil;

    /**
     * 사용자 등록
     *
     * @param request 회원가입 request
     */
    public void join(CreateUserRequest request) {
        log.debug("사용자 등록 서비스");

        User user = createUser(request);
        Center center = null;
        if (request.getRoleType().equals(RoleType.CENTER.toString())) {
            center = createCenter(request, user);
            user.setCenter(center);
        }

        isDuplicateUser(user);
        userRepository.save(user);
        if (center != null)
            centerRepository.save(center);
    }

    /**
     * User Entity 새성
     *
     * @param request 회원가입 request
     * @return User Entity
     */
    private User createUser(CreateUserRequest request) {
        String userUuid = UUID.randomUUID().toString();
        String salt = PasswordEncryptUtil.generateSalt();
        String encryptedPassword = PasswordEncryptUtil.getEncrypt(request.getPassword(), salt);
        return User.builder()
                .uuid(userUuid)
                .id(request.getId())
                .salt(salt)
                .password(encryptedPassword)
                .email(request.getEmail())
                .phone(request.getPhone())
                .name(request.getName())
                .roleType(RoleType.valueOf(request.getRoleType()))
                .build();
    }

    /**
     * Center Entity 새성
     *
     * @param request 회원가입 request
     * @param user    User Entity
     * @return Center Entity
     */
    private Center createCenter(CreateUserRequest request, User user) {
        String centerUuid = UUID.randomUUID().toString();
        return Center.builder()
                .uuid(centerUuid)
                .user(user)
                .name(request.getCenterName())
                .address(request.getCenterAddress())
                .phone(request.getCenterPhone())
                .email(request.getCenterEmail())
                .build();
    }

    /**
     * 사용자 아이디 존재 확인
     *
     * @param user 사용자 정보
     * @throws IllegalStateException 이미 존재하는 회원
     */
    private void isDuplicateUser(User user) {
        List<User> findUsers = userRepository.findUserId(user.getId());

        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 사용자 로그인
     *
     * @param request 사용자 정보
     * @return access jwt 토큰
     */
    public Token login(LoginUserRequest request) {
        try {
            String salt = userRepository.getSalt(request.getId());
            log.debug("salt = " + salt);
            String encryptedPassword = PasswordEncryptUtil.getEncrypt(request.getPassword(), salt);
            User user = userRepository.findUser(request.getId(), encryptedPassword);
            log.debug(user.toString());
            Token token = jwtAuthenticationUtil.generateAccessToken(user);
            log.debug(token.toString());
            return token;
        } catch (NullPointerException e) {
            throw new NullPointerException("사용자가 없습니다.");
        }
    }
}