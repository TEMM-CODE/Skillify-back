package com.temm.skillify.service;

import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.request.AvatarUpdateRequest;
import com.temm.skillify.model.dto.request.PasswordUpdateRequest;
import com.temm.skillify.model.dto.response.AuthenticationResponse;
import com.temm.skillify.model.dto.response.LevelProgressResponseDTO;
import com.temm.skillify.model.dto.response.StudentRankingResponseDTO;
import com.temm.skillify.model.dto.response.StudentRankingPositionDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.entity.UserAvatar;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.UserMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.UserAvatarRepository;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final GamificationService gamificationService;
    private final ClassroomRepository classroomRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<UserResponseDTO> findAllDto() {
        return findAll().stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<UserResponseDTO> findDtoById(String id) {
        return findById(id).map(userMapper::toResponseDTO);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserResponseDTO> findDtoByEmail(String email) {
        return findByEmail(email).map(userMapper::toResponseDTO);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<UserResponseDTO> findAllMentorsDto() {
        return userRepository.findByRole(UserRole.MENTOR)
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO saveAndReturnDto(User user) {
        return userMapper.toResponseDTO(save(user));
    }

    public AuthenticationResponse create(RegisterRequest request) {
        var role = UserRole.valueOf(request.getRole());
        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTel(request.getTel());
        user.setBiography(request.getBiography());
        user.setEmailNotifications(request.isEmailNotifications());
        user.setPushNotifications(request.isPushNotifications());
        user.setWeeklyReport(request.isWeeklyReport());
        user.setStudyReminder(request.isStudyReminder());
        user.setRole(role);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public UserResponseDTO createAndReturnDto(RegisterRequest request) {
        var role = UserRole.valueOf(request.getRole());
        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTel(request.getTel());
        user.setBiography(request.getBiography());
        user.setEmailNotifications(request.isEmailNotifications());
        user.setPushNotifications(request.isPushNotifications());
        user.setWeeklyReport(request.isWeeklyReport());
        user.setStudyReminder(request.isStudyReminder());
        user.setRole(role);
        user.setExpertise(request.getExpertise());
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserResponseDTO returnStudentProfile(Authentication authentication) {
        User student = getUserFromAuthentication(authentication);
        UserResponseDTO res = userMapper.toResponseDTO(student);
        Optional<UserAvatar> avatar = userAvatarRepository.findByUserId(student.getId());
        if (avatar.isPresent()) {
            res.setAvatar(avatar.get().getImageUrl());
        }
        return res;
    }

    public UserResponseDTO getUserDtoFromAuthentication(Authentication authentication) {
        return userMapper.toResponseDTO(getUserFromAuthentication(authentication));
    }

    public LevelProgressResponseDTO getLevelProgress(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return gamificationService.getLevelProgress(user);
    }

    public UserResponseDTO updateStudentProfile(Authentication authentication, RegisterRequest request) {
        User user = getUserFromAuthentication(authentication);

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getTel() != null) {
            user.setTel(request.getTel());
        }
        if (request.getBiography() != null) {
            user.setBiography(request.getBiography());
        }
        user.setEmailNotifications(request.isEmailNotifications());
        user.setPushNotifications(request.isPushNotifications());
        user.setWeeklyReport(request.isWeeklyReport());
        user.setStudyReminder(request.isStudyReminder());

        if (request.getRole() != null) {
            try {
                user.setRole(UserRole.valueOf(request.getRole()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + request.getRole());
            }
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }

    public UserResponseDTO updateStudentPassword(Authentication authentication, PasswordUpdateRequest request) {
        User user = getUserFromAuthentication(authentication);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }

    public UserResponseDTO updateStudentAvatar(Authentication authentication, AvatarUpdateRequest request) {
        User user = getUserFromAuthentication(authentication);

        Optional<UserAvatar> existingAvatar = userAvatarRepository.findByUserId(user.getId());
        UserAvatar userAvatar;

        if (existingAvatar.isPresent()) {
            userAvatar = existingAvatar.get();
            userAvatar.setImageUrl(request.getImageUrl());
        } else {
            userAvatar = new UserAvatar();
            userAvatar.setUser(user);
            userAvatar.setImageUrl(request.getImageUrl());
        }

        userAvatarRepository.save(userAvatar);
        UserResponseDTO res = userMapper.toResponseDTO(user);
        res.setAvatar(request.getImageUrl());
        return res;
    }

    public StudentRankingResponseDTO getStudentRankingByClassroom(String classroomId, Authentication authentication, int page, int size) {
        User currentStudent = getUserFromAuthentication(authentication);

        // 1) Find all students in the given classroom
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));

        // 2) Check if the authenticated student is in the classroom
        if (!classroom.getStudents().contains(currentStudent)) {
            throw new SecurityException("You are not enrolled in this classroom");
        }

        // 3) Get paginated ranking of students by XP
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "xp"));
        Page<User> studentPage = userRepository.findByIdIn(
                classroom.getStudents().stream().map(User::getId).collect(Collectors.toList()),
                pageable
        );

        // Build ranking
        List<StudentRankingPositionDTO> ranking = studentPage.getContent().stream()
                .map(user -> {
                    StudentRankingPositionDTO positionDTO = new StudentRankingPositionDTO();
                    positionDTO.userId = user.getId();
                    positionDTO.userName = user.getName();
                    positionDTO.xpAmount = user.getXp();
                    Optional<UserAvatar> avatar = userAvatarRepository.findByUserId(user.getId());
                    positionDTO.avatar = avatar.map(UserAvatar::getImageUrl).orElse(null);
                    positionDTO.position = (int) (studentPage.getNumber() * studentPage.getSize() + studentPage.getContent().indexOf(user) + 1);
                    return positionDTO;
                })
                .collect(Collectors.toList());

        // Find the authenticated student's position
        int yourPosition = 0;
        List<User> allStudents = userRepository.findByIdIn(
                classroom.getStudents().stream().map(User::getId).collect(Collectors.toList()),
                Sort.by(Sort.Direction.DESC, "xp")
        );
        for (int i = 0; i < allStudents.size(); i++) {
            if (allStudents.get(i).getId().equals(currentStudent.getId())) {
                yourPosition = i + 1;
                break;
            }
        }

        // Build response
        StudentRankingResponseDTO response = new StudentRankingResponseDTO();
        response.classroomId = classroomId;
        response.ranking = ranking;
        response.yourPosition = yourPosition;

        return response;
    }

    public List<StudentRankingResponseDTO> getStudentRankingsForAllClassrooms(Authentication authentication, int page, int size) {
        User currentStudent = getUserFromAuthentication(authentication);
    
        // Find all classrooms the student is enrolled in
        List<Classroom> classrooms = classroomRepository.findByStudentsContaining(currentStudent);
    
        // Generate ranking for each classroom
        return classrooms.stream().map(classroom -> {
            // Get paginated ranking of students by XP
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "xp"));
            Page<User> studentPage = userRepository.findByIdIn(
                    classroom.getStudents().stream().map(User::getId).collect(Collectors.toList()),
                    pageable
            );
    
            // Build ranking
            List<StudentRankingPositionDTO> ranking = studentPage.getContent().stream()
                    .map(user -> {
                        StudentRankingPositionDTO positionDTO = new StudentRankingPositionDTO();
                        positionDTO.userId = user.getId();
                        positionDTO.userName = user.getName();
                        positionDTO.xpAmount = user.getXp();
                        Optional<UserAvatar> avatar = userAvatarRepository.findByUserId(user.getId());
                        positionDTO.avatar = avatar.map(UserAvatar::getImageUrl).orElse(null);
                        positionDTO.position = (int) (studentPage.getNumber() * studentPage.getSize() + studentPage.getContent().indexOf(user) + 1);
                        return positionDTO;
                    })
                    .collect(Collectors.toList());
    
            // Find the authenticated student's position
            int yourPosition = 0;
            List<User> allStudents = userRepository.findByIdIn(
                    classroom.getStudents().stream().map(User::getId).collect(Collectors.toList()),
                    Sort.by(Sort.Direction.DESC, "xp")
            );
            for (int i = 0; i < allStudents.size(); i++) {
                if (allStudents.get(i).getId().equals(currentStudent.getId())) {
                    yourPosition = i + 1;
                    break;
                }
            }
    
            // Build response
            StudentRankingResponseDTO response = new StudentRankingResponseDTO();
            response.classroomId = classroom.getId();
            response.classroomName = classroom.getName();
            response.ranking = ranking;
            response.yourPosition = yourPosition;
    
            return response;
        }).collect(Collectors.toList());
    }
}