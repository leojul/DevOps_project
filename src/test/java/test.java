import org.junit.*;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class test {

    @Test
    public void dataframe_const1_test1() throws Exception {
        Dataframe d = new Dataframe();
        assert (d.getNumberOfColumns() == 0);
    }

    //Simple constructor test
    @Test
    public void dataframe_const2_test1() throws Exception {
        String[] str = {"{foo,bar}", "{1,2}", "{3,4}"};
        Dataframe d = new Dataframe(str);
        assertEquals("[1, 2]",d.getColumn("foo").toString());
        assertEquals("[3, 4]",d.getColumn("bar").toString());
        assertEquals(2, d.getNumberOfColumns());
    }

    //Simple constructor test with spaces that should be stripped, and empty column
    @Test
    public void dataframe_const2_test2() throws Exception {
        String[] str = {"{ foo, bar,goo }", "{1, 2 ,3}", "{4,5,6,7 }", "{4, 8 ,9 }"};
        Dataframe d = new Dataframe(str);
        assertEquals("[1, 2, 3]",d.getColumn("foo").toString());
        assertEquals("[4, 5, 6, 7]",d.getColumn("bar").toString());
        assertEquals("[4, 8, 9]",d.getColumn("goo").toString());
        assertEquals(3, d.getNumberOfColumns());
    }

    @Test
    public void dataframe_const2_test3() throws Exception {
        String[] str = {"{ foo, bar, goo }", "{1, 2 ,3}", "{string,5,7 }", "{4, 8 ,9 }"};
        try {
            Dataframe d = new Dataframe(str);
            fail("Strings should not be accepted in dataframes");
        } catch (Exception e) {
            assert (e.getMessage().contains("Only numbers are accepted"));
        }
    }

    @Test
    public void dataframe_print_test1() throws Exception {
        String[] str = {"{ foo, bar }", "{1, 2 ,3}", "{4,5,6,7 }"};
        Dataframe d = new Dataframe(str);
        final PrintStream sysOut = System.out;
        final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        d.printDataframeLines(1, 5);
        assertEquals(
                " |\tfoo |\tbar\t|\n" +
                        "1|\t1\t|\t4\t|\n" +
                        "2|\t2\t|\t5\t|\n" +
                        "3|\t3\t|\t6\t|\n" +
                        "4|\t\t|\t7\t|\n",testOut.toString());
        System.setOut(sysOut);
    }
    @Test
    public void dataframe_print_test2() throws Exception {
        String[] str = {"{ foo, bar }", "{1, 2 ,3}", "{4,5,6,7 }"};
        Dataframe d = new Dataframe(str);
        final PrintStream sysOut = System.out;
        final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        try {
            d.printDataframeLines(10, 5);
            fail("This should not run, lines should be given start to end");
        } catch (Exception e) {
            assert(e.getMessage().contains("bad argument"));
        }
    }

    @Test
    public void dataframe_doubles_test1() throws Exception {
        String[] str = {"{ column1, col2 , column_3 }", "{1.555, 2.2 ,3}", "{45, 700.5 }", "{100, 80.1 }"};
        Dataframe d = new Dataframe(str);
        assertEquals("[1.555, 2.2, 3.0]",d.getColumn("column1").toString());
        assertEquals("[45.0, 700.5]",d.getColumn("col2").toString());
        assertEquals("[100.0, 80.1]",d.getColumn("column_3").toString());
    }

    @Test
    public void dataframe_doubles_test2() throws Exception {
        String[] str = {"{ column1, col2 , column_3 }", "{1.555, 2.2 ,3}", "{0.1, .5 , -2.5  }", "{100, 80.1 }"};
        Dataframe d = new Dataframe(str);

        assertEquals("[1.555, 2.2, 3.0]",d.getColumn("column1").toString());
        assertEquals("[0.1, 0.5, -2.5]",d.getColumn("col2").toString());
        assertEquals("[100.0, 80.1]",d.getColumn("column_3").toString());
    }

    @Test
    public void dataframe_print_first_lines_test() throws Exception {
        String[] str = {"{ column1, col2 , column_3 }", "{1.555, 2.2 ,3}", "{45, 700.5 }", "{100, 80.1 }"};
        Dataframe d = new Dataframe(str);
        final PrintStream sysOut = System.out;
        final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        d.printFirstLines(2);
        assertEquals(
                " |\tcolumn1 |\tcol2 |\tcolumn_3\t|\n" +
                        "1|\t1.555\t|\t45.0\t|\t100.0\t|\n" +
                        "2|\t2.2\t|\t700.5\t|\t80.1\t|\n",testOut.toString());
        System.setOut(sysOut);
    }

    @Test
    public void dataframe_print_last_lines_test() throws Exception {
        String[] str = {"{ col1, col2 , col3 }", "{1.555, 2.2 ,3,55}", "{45, 700.5, .2 }", "{100, 80.1 }"};
        Dataframe d = new Dataframe(str);
        final PrintStream sysOut = System.out;
        final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        d.printLastLines(2);
        assertEquals(
                " |\tcol1 |\tcol2 |\tcol3\t|\n" +
                        "3|\t3.0\t|\t0.2\t|\t\t|\n" +
                        "4|\t55.0\t|\t\t|\t\t|\n",testOut.toString());
        System.setOut(sysOut);
    }
    @Test
    public void dataframe_print_dataframe_test() throws Exception {
        String[] str = {"{ col1, col2 , col3 }", "{1.555, 2.2 ,3,55}", "{45, 700.5, .2 }", "{100, 80.1 }"};
        Dataframe d = new Dataframe(str);
        final PrintStream sysOut = System.out;
        final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        d.printDataframe();
        assertEquals(
                " |\tcol1 |\tcol2 |\tcol3\t|\n" +
                        "1|\t1.555\t|\t45.0\t|\t100.0\t|\n" +
                        "2|\t2.2\t|\t700.5\t|\t80.1\t|\n" +
                        "3|\t3.0\t|\t0.2\t|\t\t|\n" +
                        "4|\t55.0\t|\t\t|\t\t|\n",testOut.toString());
        System.setOut(sysOut);
    }

    @Test
    public void dataframe_column_min_test() throws Exception {
        String[] str = {"{ col1, col2 , col3 }", "{1.555, 2.2 ,3,55}", "{45, 700.5, .2 }", "{100, 80 }"};
        Dataframe d = new Dataframe(str);
        assertEquals(1.555 , d.columnMinimumValue("col1"),0);
        assertEquals(0.2 , d.columnMinimumValue("col2"),0);
        assertEquals(80 , d.columnMinimumValue("col3"),0);
    }

    @Test
    public void dataframe_column_max_test() throws Exception {
        String[] str = {"{ col1, col2 , col3 }", "{1.555, 2.2 ,3,55}", "{45, 700.5, .2 }", "{100, 80 }"};
        Dataframe d = new Dataframe(str);
        assertEquals(55 , d.columnMaximumValue("col1"),0);
        assertEquals(700.5 , d.columnMaximumValue("col2"),0);
        assertEquals(100 , d.columnMaximumValue("col3"),0);

    }

    @Test
    public void dataframe_create_from_columns_test() throws Exception {
        String[] str = {"{ col1, col2 , col3 }", "{1.555, 2.2 ,3,55}", "{45, 700.5, .2 }", "{100, 80.1 }"};
        Dataframe d = new Dataframe(str);
        String[] columns = {"col1"};
        Dataframe nd = d.newDataframeFromColumns(columns);
        assertEquals(nd.getNumberOfColumns() ,1);
        assertEquals(1.555 , nd.columnMinimumValue("col1"),0);
        assertEquals(55 , nd.columnMaximumValue("col1"),0);
    }

    @Test
    public void dataframe_create_from_lines_test() throws Exception {
        String[] str = {"{ col1, col2 , col3 }", "{1.555, 2.2 ,3,55}", "{45, 700.5, .2 }", "{100, 80.1 }"};
        Dataframe d = new Dataframe(str);
        String[] columns = {"col1"};
        Dataframe nd = d.newDataframeFromLines(2, 3);
        assertEquals (nd.getNumberOfColumns() , 3);
        assertEquals (2.2 , nd.columnMinimumValue("col1"),0);
        assertEquals (2.2 , nd.columnMaximumValue("col1"),0);
        assertEquals (700.5 , nd.columnMinimumValue("col2"),0);
        assertEquals (80.1, nd.columnMaximumValue("col3"),0);
        assertEquals (80.1, nd.columnMinimumValue("col3"),0);
    }

    @Test
    public void dataframe_calculate_mean_test() throws Exception {
        String[] str = {"{ col1, col2 , col3 }", "{1.555, 2.2 ,3,55}", "{100, 700 }", "{100, 80.1 }"};
        Dataframe d = new Dataframe(str);
        assertEquals(15.43875, d.columnMeanValue("col1"),0);
        assertEquals(400, d.columnMeanValue("col2"),0);
        assertEquals(90.05, d.columnMeanValue("col3"),0);
    }

    @Test
    public void dataframe_const3_test1() throws Exception {
        Dataframe d = new Dataframe("Dataframe.csv");
        assertEquals(4,d.getNumberOfColumns());
        assertEquals(4 , d.columnMaximumValue("col1"),0);
        assertEquals(2.8, d.columnMinimumValue("col2"),0);
        assertEquals(185, d.columnMeanValue("col3"),0);
        assertEquals(10,d.columnMinimumValue("col4"),0);
    }
}
