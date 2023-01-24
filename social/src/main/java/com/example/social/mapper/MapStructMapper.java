package com.example.social.mapper;

import com.example.social.dto.UserInfoDto;
import com.example.social.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;


@Mapper
public interface MapStructMapper {
    MapStructMapper MAPPER = Mappers.getMapper(MapStructMapper.class);
    UserInfoDto mapToUserDto(User user);
    User mapToUser(UserInfoDto userDto);

}
