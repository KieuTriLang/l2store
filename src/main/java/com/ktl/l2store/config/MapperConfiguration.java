package com.ktl.l2store.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ktl.l2store.dto.EvaluateDto;
import com.ktl.l2store.dto.ProductOverviewDto;
import com.ktl.l2store.dto.UserDto;
import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;

@Configuration
public class MapperConfiguration {

        @Bean
        public ModelMapper modelMapper() {
                ModelMapper modelMapper = new ModelMapper();

                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD)
                                .setSkipNullEnabled(true);

                // User - UserDto
                modelMapper.createTypeMap(User.class, UserDto.class).addMapping(u -> u.getAvatar().getFileCode(),
                                UserDto::setAvatarUri);

                // Product - ProductDto
                modelMapper.createTypeMap(Product.class, ProductOverviewDto.class).addMapping(
                                p -> p.getImage().getFileCode(),
                                ProductOverviewDto::setImageUri);

                // Evaluate - EvaluateDto
                modelMapper.addMappings(new PropertyMap<Evaluate, EvaluateDto>() {

                        @Override
                        protected void configure() {
                                // TODO Auto-generated method stub
                                map().setUserName(source.getUser().getUsername());
                                map().setAvatarUri(source.getUser().getAvatar().getFileCode().toString());
                        }

                });
                return modelMapper;
        }
}
