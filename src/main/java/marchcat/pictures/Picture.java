package marchcat.pictures;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "pictures")
public class Picture {

	@Id
	private int id;
	
	@Column("name")
	private String name;
	@Column("ext")
	private String ext;
	@Column("hashName")
	private String hashName;	
	@Column("dou")
	private Timestamp timestamp;
	@Column("storage")
	private int storage;
	@Column("link")
	private String link;

	public int getId() {
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

	public String getHashName() {
		return hashName;
	}

	public void setHashName(String hashName) {
		this.hashName = hashName;
	}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String getLink() {
		return link;
	}
	
	
}
