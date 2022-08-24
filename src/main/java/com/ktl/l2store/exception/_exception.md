# Exception folder

> Handle all exception, make errors more obvious

## Example

###### **_ApiException.java_**

```java
@AllArgsConstructor
@Data
public class ApiException {
    private HttpStatus httpStatus;
    private String message;
    private ZonedDateTime timestamp;
}
```
###### **_product/ProductNotfoundException.java_**

```java
public class ProductNotfoundException extends RuntimeException {
    public ProductNotfoundException(String message){
        super(message);
    }
}
```
###### **_product/ProductExceptionHandler.java_**

```java
@ControllerAdvice
public class ProductExceptionHandler{
    @ExceptionHandler(value ={ProductNotfoundException.class})
    public ResponseEntity<Object> productNotfoundException(ProductNotfoundException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
            badRequest,
            e.getMessage(),
            ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException,badRequest);
    }
}
```
## To do
- [ ] User
- [ ] Product