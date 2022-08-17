# Service folder

> Contains all your service of project

## Example

###### **_ProductService.java_**

```java
public interface ProductService{
    Product saveProduct(Product product);
    boolean existProduct(int id);
    List<Product> getProducts();
    ...
}

```
###### **_ProductServiceImpl.java_**

```java
@Service
@RequiredArgsConstructor
@Transactional
public ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepo productRepo;

    @Overide
    public Product saveProduct(Product product){
        return productRepo.save(product);
    }
    @Overide
    public boolean existProduct(int id){
        return null;
    }
    @Overide
    public List<Product> getProducts(){
        return null;
    }
    ...
}

```
## To do
- [ ] User
- [ ] Product