import org.junit.Assert;
import org.junit.Test;

public class MainTest {
    @Test
    public void testGetUglyCount() throws Exception {
        Assert.assertEquals(0, Main.getUglyCount("1"));
        Assert.assertEquals(1, Main.getUglyCount("9"));
        Assert.assertEquals(6, Main.getUglyCount("011"));
        Assert.assertEquals(64, Main.getUglyCount("12345"));
    }
}