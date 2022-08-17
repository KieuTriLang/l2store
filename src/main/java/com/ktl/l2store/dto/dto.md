# Dto folder

> Contains ***ResponseDto*** , ***RequestDto***

## Example

###### **_request/ReqProductDto.java_**

```java
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqProductDto{
    private int id;
    private String productCode;
    private String name;
}

```
## To do
- [ ] Request
  - [ ] ReqUserDto
- [ ] Response
  - [ ] ResUserDto