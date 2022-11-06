package com.ktl.l2store.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ktl.l2store.dto.ComboProductDetailDto;
import com.ktl.l2store.dto.ComboProductOverviewDto;
import com.ktl.l2store.dto.EvaluateDto;
import com.ktl.l2store.dto.OrderDetailDto;
import com.ktl.l2store.dto.OrderItem;
import com.ktl.l2store.dto.OrderOverviewDto;
import com.ktl.l2store.dto.ProductDetailDto;
import com.ktl.l2store.dto.ProductOverviewDto;
import com.ktl.l2store.dto.UserDto;
import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.OCombo;
import com.ktl.l2store.entity.OProduct;
import com.ktl.l2store.entity.Order;
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
                modelMapper.createTypeMap(User.class, UserDto.class)
                                .addMappings(new PropertyMap<User, UserDto>() {

                                        @Override
                                        protected void configure() {

                                                map().convertDate(source.getDob());
                                                map().setAvatarUri(source.getAvatar().getFileCode().toString());
                                        }

                                });
                // Product - ProductOverviewDto
                modelMapper.createTypeMap(Product.class, ProductOverviewDto.class)
                                .addMappings(new PropertyMap<Product, ProductOverviewDto>() {

                                        @Override
                                        protected void configure() {

                                                map().setSold(source.getTotalPurchases());
                                                map().setImageUri(source.getImage().getFileCode().toString());
                                                map().setaoe(source.getEvaluates());
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
                                                map().setProductName((source.getProduct().getName()));
                                                map().setAvatarUri(
                                                                source.getUser().getAvatar().getFileCode().toString());
                                                map().convertDate(source.getPostedTime());

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

                // Order - OrderOverviewDto
                modelMapper.createTypeMap(Order.class, OrderOverviewDto.class)
                                .addMappings(new PropertyMap<Order, OrderOverviewDto>() {
                                        @Override
                                        protected void configure() {
                                                map().setAmountOfP(source.getOrderProducts());
                                                map().setAmountOfC(source.getOrderCombos());
                                                map().setBuyer(source.getOwner().getUsername());
                                                map().convertCreatedDate(source.getCreatedTime());
                                                map().convertPaymentDate(source.getPaymentTime());
                                        }
                                });
                // Order - OrderDetailDto
                modelMapper.createTypeMap(Order.class, OrderDetailDto.class)
                                .addMappings(new PropertyMap<Order, OrderDetailDto>() {
                                        @Override
                                        protected void configure() {
                                                map().convertCreatedDate(source.getCreatedTime());
                                                map().convertPaymentDate(source.getPaymentTime());
                                                map().setBuyerName(source.getOwner());
                                        }
                                });
                modelMapper.createTypeMap(OCombo.class, OrderItem.class)
                                .addMappings(new PropertyMap<OCombo, OrderItem>() {
                                        @Override
                                        protected void configure() {
                                                map().setName(source.getComboProduct().getName());
                                                map().setPrice(source.getComboProduct().getTotalPrice());
                                                map().setQuantity(source.getQuantity());
                                        }
                                });
                modelMapper.createTypeMap(OProduct.class, OrderItem.class)
                                .addMappings(new PropertyMap<OProduct, OrderItem>() {
                                        @Override
                                        protected void configure() {
                                                map().setName(source.getProduct().getName());
                                                map().setPrice(source.getProduct().getPrice());
                                                map().setQuantity(source.getQuantity());
                                        }
                                });

                return modelMapper;
        }
}
