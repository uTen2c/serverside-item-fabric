![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Futen2c.github.io%2Frepo%2Fdev%2Futen2c%2Fserverside-item-fabric%2Fmaven-metadata.xml)

# ServerSideItem

サーバーサイドのみで新しいアイテムを追加するやつ

### Groovy DSL
```groovy
repositories {
  maven { url 'https://uten2c.github.io/repo/' }
}

dependencies {
  implementation 'dev.uten2c:serverside-item-fabric:VERSION'
}
```

### Kotlin DSL
```kotlin
repositories {
  maven("https://uten2c.github.io/repo/")
}

dependencies {
  implementation("dev.uten2c:serverside-item-fabric:VERSION")
}
```

### Example

```java
public class ExampleMod implements ModInitializer { 
    
    public static final Item TEST_ITEM = new TestItem(new Item.Settings());

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("example", "test_item"), TEST_ITEM);
    }
}

public class TestItem extends Item implements ServerSideItem {

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getVisualItem() {
        return Items.DIAMOND;
    }
}
```

Get item
```
/give @s example:test_item
```