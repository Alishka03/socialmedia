package com.example.social.services;

import com.example.social.dto.UserInfoDto;
import com.example.social.dto.UserUpdateInfoDTO;
import com.example.social.entities.User;
import com.example.social.exception.InvalidOperationException;
import com.example.social.mapper.UserMapper;
import com.example.social.pojo.PostResponse;
import com.example.social.repository.UserRepository;
import com.example.social.util.FileNamingUtil;
import com.example.social.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;

    private String PATH_DIRECTORY = "C:\\Users\\Admin\\Desktop\\social-media-front-back\\front-end-final-project\\src\\assets\\images";

    public Optional<User> userById(int id) {
        Optional<User> user = userRepository.findById(id);
        System.out.println(user.get().getUsername());
        if (user.isEmpty()) {
            throw new InvalidOperationException("There is no user with id : " + id);
        }
        return user;
    }

    public Optional<User> userByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public final User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userByUsername(username).get();
    }

    @Transactional
    public User updateImage(MultipartFile postPhoto) {
        User user = getAuthenticatedUser();
        if (postPhoto != null && postPhoto.getSize() > 0) {
            System.out.println("PATH_DIRECTORY : " + PATH_DIRECTORY);
            String newPhotoName = fileNamingUtil.nameFile(postPhoto);
            String newPhotoUrl = newPhotoName;
            user.setProfilePhoto(newPhotoUrl);
            try {
                fileUploadUtil.saveNewFile(PATH_DIRECTORY, newPhotoName, postPhoto);
                System.out.println("File saved");
            } catch (IOException e) {
                System.out.println("PATH NOT FOUND");
                throw new RuntimeException();
            }
        }
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void followUser(int userId) {
        User authUser = getAuthenticatedUser();
        if (authUser.getId() != userId) {
            User userToFollow = userById(userId).get();
            authUser.getFollowingUsers().add(userToFollow);
            userToFollow.getFollowerUsers().add(authUser);

        } else {
            throw new InvalidOperationException();
        }
    }

    public void testing() {
        System.out.println("SOMETHING");
    }

    @Transactional
    public void updateUser(UserInfoDto userInfoDto) {
        System.out.println(userInfoDto);
    }

    @Transactional
    public void unfollowUser(int userId) {
        User authUser = getAuthenticatedUser();
        if (authUser.getId() != (userId)) {
            User userToUnfollow = userById(userId).get();
            authUser.getFollowingUsers().remove(userToUnfollow);
            userToUnfollow.getFollowerUsers().remove(authUser);

        } else {
            throw new InvalidOperationException();
        }
    }

    @Transactional
    public User updateProfile(UserUpdateInfoDTO userUpdateInfoDTO) {
        User authUser = getAuthenticatedUser();
        if (!userUpdateInfoDTO.getHometown().equals("")) {
            authUser.setHometown(userUpdateInfoDTO.getHometown());
        }
        if (!userUpdateInfoDTO.getIntro().equals("")) {
            authUser.setIntro(userUpdateInfoDTO.getIntro());
        }
        if (!userUpdateInfoDTO.getWorkplace().equals("")) {
            authUser.setWorkplace(userUpdateInfoDTO.getWorkplace());
        }
        if (!userUpdateInfoDTO.getSurname().equals("")) {
            authUser.setSurname(userUpdateInfoDTO.getSurname());
        }
        if (!userUpdateInfoDTO.getName().equals("")) {
            authUser.setName(userUpdateInfoDTO.getName());
        }
        userRepository.save(authUser);
        return authUser;
    }
    @Transactional
    public User updateProfileAdmin(UserUpdateInfoDTO userUpdateInfoDTO,int id) {
        User authUser = userRepository.findById(id).get();
        if (!userUpdateInfoDTO.getHometown().equals("")) {
            authUser.setHometown(userUpdateInfoDTO.getHometown());
        }
        if (!userUpdateInfoDTO.getIntro().equals("")) {
            authUser.setIntro(userUpdateInfoDTO.getIntro());
        }
        if (!userUpdateInfoDTO.getWorkplace().equals("")) {
            authUser.setWorkplace(userUpdateInfoDTO.getWorkplace());
        }
        if (!userUpdateInfoDTO.getSurname().equals("")) {
            authUser.setSurname(userUpdateInfoDTO.getSurname());
        }
        if (!userUpdateInfoDTO.getName().equals("")) {
            authUser.setName(userUpdateInfoDTO.getName());
        }
        if (!userUpdateInfoDTO.getPassword().equals("")) {
            authUser.setPassword(userUpdateInfoDTO.getPassword());
        }
        userRepository.save(authUser);
        return authUser;
    }

    @Transactional
    public List<User> search(String value){
        return userRepository.findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContaining(value,value);
    }

    @Transactional
    public void deleteUser(int id){
        Optional<User> targetUser = userRepository.findById(id);
        if(targetUser.isEmpty()){
            throw new InvalidOperationException("User not found with id :" + id);
        }else{
            userRepository.deleteById(id);
        }
    }
}
