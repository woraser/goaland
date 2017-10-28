package com.anosi.asset.model.mongo;

import java.io.Serializable;
import java.math.BigInteger;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class AbstractDocument implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -461874198015860060L;
	
	@Id
	private BigInteger id;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}
	
	public static BigInteger ObjectIdToBigIntegerConverter(ObjectId source) {
		return source == null ? null : new BigInteger(source.toString(), 16);
	}
	
	public static ObjectId BigIntegerToObjectIdConverter(BigInteger source) {
		return source == null ? null : new ObjectId(source.toString(16));
	}
	
	/***
	 * 由于前端不能识别bigInteger这种大数字，需要提供一个id的string版本
	 * 
	 * @return
	 */
	public String getStringId(){
		if(id!=null){
			return id.toString();
		}
		return null;
	}
	
}
