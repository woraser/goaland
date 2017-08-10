package com.anosi.asset.test

import static org.junit.Assert.*

import org.junit.Test

import com.anosi.asset.util.FileFetchUtil.Suffix


class EnumTest {

	@Test
	void testSuffix(){
		Suffix.valueOf("TXT")
	}
	
}
