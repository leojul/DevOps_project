import org.junit.*;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class test {

    //Simple constructor test
    @Test
    public void dataframe_const1_test1() throws Exception {
        String[] str = {"{foo,bar}", "{1,2}", "{3,4}"};
        Dataframe d = new Dataframe(str);
        assertEquals("[1, 2]",d.getColumn("foo").toString());
        assertEquals("[3, 4]",d.getColumn("bar").toString());
        assertEquals(2, d.getNumberOfColumns());
    }

    //Simple constructor test with spaces that should be stripped, and empty column
    @Test
    public void dataframe_const1_test2() throws Exception {
        String[] str = {"{ foo, bar,goo }", "{1, 2 ,3}", "{4,5,6,7 }", "{ }"};
        Dataframe d = new Dataframe(str);
        assertEquals("[1, 2, 3]",d.getColumn("foo").toString());
        assertEquals("[4, 5, 6, 7]",d.getColumn("bar").toString());
        assertEquals("[]",d.getColumn("goo").toString());
        assertEquals(3, d.getNumberOfColumns());
    }

    @Test
    public void dataframe_print_test1(){
        String[] str = {"{ foo, bar,goo }", "{1, 2 ,3}", "{4,5,6,7 }", "{ }"};
        Dataframe d = new Dataframe(str);
        final PrintStream sysOut = System.out;
        final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        d.printDataframeLines(1,5);
        assertEquals(" |\tfoo |\tbar |\tgoo\t|\n" +
                "1|\t1\t|\t4\t|\t\t|\n" +
                "2|\t2\t|\t5\t|\t\t|\n" +
                "3|\t3\t|\t6\t|\t\t|\n" +
                "4|\t\t|\t7\t|\t\t|\n",
                testOut.toString());
        System.setOut(sysOut);
    }


}
