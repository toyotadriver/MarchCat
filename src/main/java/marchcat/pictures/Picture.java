package marchcat.pictures;

import org.springframework.data.annotation.Id;

public class Picture {

	@Id
	private int id;
	
	private String name;
	
	private String ext;
	
	private String rnd_name;	

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getRnd_name() {
		return rnd_name;
	}

	public void setRnd_name(String rnd_name) {
		this.rnd_name = rnd_name;
	}
	
	
}
