# Entity folder

> Contains all your entity model of project

> ###### - The fields in the entity are the fields in the database

## Example

###### **_Product.java_**

```java
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product{
    @Id
    @GeneratedValue(strategy = GeneratedType.IDENTITY)
    private int id;
    private String productCode;
    private String name;
    private float price;
}

```
## To do
- [ ] User
- [ ] Product