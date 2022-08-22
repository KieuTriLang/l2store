# Api folder

> Define api path 

## Example

###### **_ProductController.java_**

```java
@RestController
@RequestMapping("/api/products")
public class ProductController{
    
    @Autowired
    private final ProductService productService;

    @RequestMapping(
        value = "/{id}", 
        method = RequestMethod.GET
    )
    public ResponseEntity<...> getProductById(@PathVariable("id") String id) {
        // code logic here
        return new ResponseEntity<>(..., HttpStatus.OK);
    }
}

```

## To Do

- [ ] UserController
- [ ] ProductController
- [ ] PaymentController