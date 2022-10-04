package com.ktl.l2store.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ktl.l2store.dto.ComboProductDto;
import com.ktl.l2store.dto.EvaluateDto;
import com.ktl.l2store.dto.OrderComboDto;
import com.ktl.l2store.dto.OrderOverviewDto;
import com.ktl.l2store.dto.OrderProductDto;
import com.ktl.l2store.dto.ProductOverviewDto;
import com.ktl.l2store.dto.UserDto;
import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.Order;
import com.ktl.l2store.entity.OrderCombo;
import com.ktl.l2store.entity.OrderProduct;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;

@Configuration
public class MapperConfiguration {

        @Bean
        public ModelMapper modelMapper() {
                ModelMapper modelMapper = new ModelMapper();

                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD)
                                .setSkipNullEnabled(true).setAmbiguityIgnored(true);

                // User - UserDto
                modelMapper.createTypeMap(User.class, UserDto.class).addMapping(u -> u.getAvatar().getFileCode(),
                                UserDto::setAvatarUri);

                // Product - ProductDto
                modelMapper.createTypeMap(Product.class, ProductOverviewDto.class).addMapping(
                                p -> p.getImage().getFileCode(),
                                ProductOverviewDto::setImageUri);

                // Evaluate - EvaluateDto
                modelMapper.createTypeMap(Evaluate.class, EvaluateDto.class)
                                .addMappings(new PropertyMap<Evaluate, EvaluateDto>() {

                                        @Override
                                        protected void configure() {

                                                map().setUserName(source.getUser().getUsername());
                                                map().setAvatarUri(
                                                                source.getUser().getAvatar().getFileCode().toString());
                                        }

                                });

                // ComboProduct - ComboProductDto
                modelMapper.createTypeMap(ComboProduct.class, ComboProductDto.class)
                                .addMappings(new PropertyMap<ComboProduct, ComboProductDto>() {

                                        @Override
                                        protected void configure() {
                                                // map().setCreatorName(source.getCreator().getFirstName());
                                                // map().setCreatorAvatarUri(
                                                // source.getCreator().getAvatar().getFileCode()
                                                // .toString());

                                                map().setProductImages(source.getProducts());
                                        }
                                });

                // Order - OrderOverviewDto
                // modelMapper.createTypeMap(Order.class, OrderOverviewDto.class)
                // .addMappings(new PropertyMap<Order, OrderOverviewDto>() {
                // @Override
                // protected void configure() {
                // map().setAmountOfCombo(source.getOrderCombos().size());
                // map().setAmountOfProduct(source.getOrderProducts().size());
                // }
                // });

                // Order - OrderDetailDto
                modelMapper.createTypeMap(Order.class, OrderOverviewDto.class)
                                .addMappings(new PropertyMap<Order, OrderOverviewDto>() {
                                        @Override
                                        protected void configure() {
                                                map().setAmountOfCombo(source.getOrderCombos() != null
                                                                ? source.getOrderCombos().size()
                                                                : 0);
                                                map().setAmountOfProduct(source.getOrderProducts() != null
                                                                ? source.getOrderProducts().size()
                                                                : 0);
                                        }
                                });

                // OrderProduct - OrderProductDto
                modelMapper.createTypeMap(OrderProduct.class, OrderProductDto.class)
                                .addMappings(new PropertyMap<OrderProduct, OrderProductDto>() {
                                        @Override
                                        protected void configure() {
                                                map().setNameProduct(source.getProduct().getName());
                                                map().setPrice(source.getProduct().getPrice());
                                        }
                                });
                // OrderCombo - OrderComboDto
                modelMapper.createTypeMap(OrderCombo.class, OrderComboDto.class)
                                .addMappings(new PropertyMap<OrderCombo, OrderComboDto>() {
                                        @Override
                                        protected void configure() {
                                                map().setNameCombo(source.getComboProduct().getName());
                                                map().setPrice(source.getComboProduct().getTotalPrice());
                                        }
                                });
                return modelMapper;
        }
}
