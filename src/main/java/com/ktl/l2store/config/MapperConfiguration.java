package com.ktl.l2store.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ktl.l2store.dto.ComboProductDetailDto;
import com.ktl.l2store.dto.ComboProductOverviewDto;
import com.ktl.l2store.dto.EvaluateDto;
import com.ktl.l2store.dto.OrderComboDto;
import com.ktl.l2store.dto.OrderOverviewDto;
import com.ktl.l2store.dto.OrderProductDto;
import com.ktl.l2store.dto.ProductDetailDto;
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

                // Product - ProductOverviewDto
                modelMapper.createTypeMap(Product.class, ProductOverviewDto.class)
                                .addMappings(new PropertyMap<Product, ProductOverviewDto>() {

                                        @Override
                                        protected void configure() {

                                                map().setSold(source.getTotalPurchases());
                                                map().setImageUri(source.getImage().getFileCode().toString());
                                        }

                                });
                // Product - ProductDetailDto
                modelMapper.createTypeMap(Product.class, ProductDetailDto.class)
                                .addMappings(new PropertyMap<Product, ProductDetailDto>() {

                                        @Override
                                        protected void configure() {

                                                map().setSold(source.getTotalPurchases());
                                                map().setImageUri(source.getImage().getFileCode().toString());
                                        }

                                });

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

                // ComboProduct - ComboProductOverviewDto
                modelMapper.createTypeMap(ComboProduct.class, ComboProductOverviewDto.class)
                                .addMappings(new PropertyMap<ComboProduct, ComboProductOverviewDto>() {

                                        @Override
                                        protected void configure() {
                                                map().setProductImagesUrl(source.getProducts());
                                        }
                                });
                // ComboProduct - ComboProductDetailDto
                modelMapper.createTypeMap(ComboProduct.class, ComboProductDetailDto.class);

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
