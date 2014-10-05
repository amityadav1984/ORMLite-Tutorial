package pojo;

import wrapper.CustomBaseDaoImpl;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//Add daoClass
//Fixed: Caused by: java.lang.ClassCastException: com.j256.ormlite.dao.BaseDaoImpl$5 cannot be cast to com.kore.korelib.dao.CustomDao
//Now OrmLiteSqliteOpenHelper.getDao() will return "CustomBaseDaoImpl" instead "BaseDaoImpl"
//In DatabaseHelper we are using CustomDao<T, ID>
//Since native Dao<T, ID> interface is not having getCursor() method
@DatabaseTable(tableName = "EMPLOYE", daoClass = CustomBaseDaoImpl.class)
public class Employe {

	/**
	 * if generatedId = true only applicable for int
	 */
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String fname;
	
	@DatabaseField
	private String lname;
	
	@DatabaseField
	private String designation = "Architect";
	
	@DatabaseField(canBeNull = false)
    private String password ="******";	
	
	
	/**
	 * if we not annotate it (not adding @DatabaseField)
	 * field will not be persisted
	 */
	private String address;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
