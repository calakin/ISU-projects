package units;

public class UnitTest {
	public static void main(String[] args) {
		Unit unit = new Giant();
		Giant giant = new Giant();
		
		System.out.println(unit.sameUnitType(giant));
	}
}
