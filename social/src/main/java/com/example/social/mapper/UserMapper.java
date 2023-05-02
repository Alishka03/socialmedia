package com.example.social.mapper;

import com.example.social.dto.UserInfoDto;
import com.example.social.dto.UserResponseDto;
import com.example.social.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserInfoDto toDTO(User user);
}
