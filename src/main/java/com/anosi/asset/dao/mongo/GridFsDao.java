package com.anosi.asset.dao.mongo;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/***
 * gridFs
 * @author jinyao
 *
 */
@Repository("gridFsDao")
public class GridFsDao {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private GridFS gridFS;
	
	@PostConstruct
	public void init(){
		gridFS=new GridFS(mongoTemplate.getDb());
	}
	
	public Object uploadFileToGridFS(InputStream in,String fileName) throws Exception{
		GridFSInputFile inputFile = gridFS.createFile(in, true);
		inputFile.setFilename(fileName);
		inputFile.save();
		return inputFile.getId();
	}
	
	public InputStream getFileFromGridFS(ObjectId objectId){
		GridFSDBFile gridFSDBFile = gridFS.findOne(objectId);
		InputStream input = null;
		if(gridFSDBFile!=null){
			input=gridFSDBFile.getInputStream();
		}
		return input;
	}
	
	public void deleteFileFromGridFS(ObjectId objectId){
		gridFS.remove(objectId);
	}
	
}
