import static org.junit.Assert.assertEquals;

import org.junit.*;


public class test {
	int x = 2;
	int y =3;
	/*@Test
	public void trucchose() throws Exception{
		assertEquals(jeveuxça,enutilisantcettemethode(x,y));
	}*/
	@Test
	public void trucchose() throws Exception{
		assertEquals(5,x+y);
	}
	@Test
	public void trucchose2() throws Exception{
		assertEquals(6,x+y);
	}
}
