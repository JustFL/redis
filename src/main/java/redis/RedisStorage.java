package redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

class Student implements Serializable{

	private static final long serialVersionUID = 8542128939087461701L;
	
	private int id;
	private String name;
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
	@Override
	public String toString() {
		return "student [id=" + id + ", name=" + name + "]";
	}
	public Student(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Student() {
		super();
	}
	
}

public class RedisStorage {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		RedisStorage r = new RedisStorage();
		r.save();
		r.take();
		r.gsonStorage();
	}
	
	public static Jedis getConn() {
		Jedis conn = new Jedis("hadoop02", 6379);
		return conn;
	}
	
	//对象存
	public void save() throws IOException {
		
		Jedis conn = getConn();
		
		Student student = new Student(2, "xuzheng");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(student);
		
		byte[] byteArray = baos.toByteArray();
		conn.set("student".getBytes(), byteArray);
		
		conn.close();
	}
	
	//对象取
	public void take() throws IOException, ClassNotFoundException {
		
		Jedis conn = getConn();
		
		byte[] b = conn.get("student".getBytes());
		
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Student stu = (Student)ois.readObject();
		
		System.out.println(stu);
		conn.close();
	}
	
	public void gsonStorage() {
		
		Jedis jedis = getConn();
		Student student = new Student(1, "luhan");
		
		Gson gson = new Gson();
		String pJson = gson.toJson(student);
		//	将json串存入redis
		jedis.set("student1", pJson);
		// 从redis中取出对象的json串
		String pJsonResp = jedis.get("student1");
		// 将返回的json解析成对象
		Student luhan = gson.fromJson(pJsonResp, Student.class);
		// 显示对象的属性
		System.out.println(luhan);

		
	}
	
}
