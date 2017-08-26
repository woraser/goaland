package com.anosi.asset.test;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;

import org.junit.Test;

public class EnumTestJAVA {

	enum Color{
		RED("red",(long)5),BLUE("blue",(long)5),GREEN("green",(long)5);
		
		private String name;
		
		private Long size;


		private Color(String name, Long size) {
			this.name = name;
			this.size = size;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getSize() {
			return size;
		}

		public void setSize(Long size) {
			this.size = size;
		}
		
	}
	
	@Test
	public void testEnum(){
		Color color = Color.valueOf("RED");
		System.out.println(color.getSize());
		EnumSet<Color> enumSet = EnumSet.allOf(Color.class);
		for (Color color2 : enumSet) {
			System.out.println(color2.getName());
		}
		EnumMap<Color,Object> enumMap = new EnumMap<>(Color.class);
		enumMap.put(Color.RED, "星期一");
		enumMap.put(Color.BLUE, "星期二");
		for (Entry<Color,Object> entry : enumMap.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
	}
	
}
