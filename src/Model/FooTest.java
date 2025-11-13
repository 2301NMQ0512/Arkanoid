
package model;

import org.junit.Assert;
import org.junit.Test;

public class FooTest {

    @Test
    public void testToString() {
        String expected = "Test";
        model.Foo foo = new model.Foo(expected);
        Assert.assertEquals(expected, foo.toString());
    }
}
