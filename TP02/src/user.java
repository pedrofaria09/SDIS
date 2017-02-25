
public class user {
	private String name;
	private String plate;
	
	public user(String name,String plate) {
		this.name=name;
		this.plate=plate;
	}

	public String getUserName(){
		return name;
	}
	
	public String getUserPlate(){
		return plate;
	}
	
}
